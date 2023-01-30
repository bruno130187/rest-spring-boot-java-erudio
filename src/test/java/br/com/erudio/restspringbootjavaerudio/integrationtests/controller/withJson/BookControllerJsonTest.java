package br.com.erudio.restspringbootjavaerudio.integrationtests.controller.withJson;

import br.com.erudio.restspringbootjavaerudio.configs.TestConfigs;
import br.com.erudio.restspringbootjavaerudio.data.vo.security.AccountCredentialsVO;
import br.com.erudio.restspringbootjavaerudio.data.vo.security.TokenVO;
import br.com.erudio.restspringbootjavaerudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.restspringbootjavaerudio.integrationtests.vo.BookVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource("classpath:testcontainers.properties")
public class BookControllerJsonTest extends AbstractIntegrationTest{
	private static RequestSpecification requestSpecification;
	private static ObjectMapper objectMapper;
	private static BookVO bookVO;
	private final String user = "leandro";
	private final String pass = "123456";
	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		bookVO = new BookVO();
	}
	@Test
	@Order(0)
	public void testAuthorization() throws JsonMappingException, JsonProcessingException {

		AccountCredentialsVO accountCredentialsVO = new AccountCredentialsVO(user, pass);

		var accessToken = given()
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(accountCredentialsVO)
					.when()
					.post()
				.then()
					.statusCode(200 )
					.extract()
					.body().as(TokenVO.class)
					.getAccessToken();

		requestSpecification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCALHOST)
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

	}
	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		mockBook();
		
		var content = given()
				.spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(bookVO)
				.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
							.body()
								.asString();


		BookVO persistedBookVO = objectMapper.readValue(content, BookVO.class);
		bookVO = persistedBookVO;
		
		assertNotNull(persistedBookVO);
		
		assertNotNull(persistedBookVO.getId());
		assertNotNull(persistedBookVO.getAuthor());
		assertNotNull(persistedBookVO.getLaunchDate());
		assertNotNull(persistedBookVO.getPrice());
		assertNotNull(persistedBookVO.getTitle());
		
		assertTrue(persistedBookVO.getId() > 0);

		GregorianCalendar gc = new GregorianCalendar(2023, 01, 27);

		assertEquals("Author Book", persistedBookVO.getAuthor());
		assertEquals(gc.getTime(), persistedBookVO.getLaunchDate());
		assertEquals(BigDecimal.TEN, persistedBookVO.getPrice());
		assertEquals("Title Book", persistedBookVO.getTitle());
	}

	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		bookVO.setAuthor("Author Book Updated");

		var content = given()
				.spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.body(bookVO)
					.when()
					.post()
				.then()
				.statusCode(200)
					.extract()
					.body()
						.asString();

		BookVO persistedBookVO = objectMapper.readValue(content, BookVO.class);
		bookVO = persistedBookVO;

		assertNotNull(persistedBookVO);

		assertNotNull(persistedBookVO.getId());
		assertNotNull(persistedBookVO.getAuthor());
		assertNotNull(persistedBookVO.getLaunchDate());
		assertNotNull(persistedBookVO.getPrice());
		assertNotNull(persistedBookVO.getTitle());

		assertEquals(persistedBookVO.getId(), bookVO.getId());

		assertTrue(persistedBookVO.getId() > 0);

		GregorianCalendar gc = new GregorianCalendar(2023, 01, 27);

		assertEquals("Author Book Updated", persistedBookVO.getAuthor());
		assertEquals(gc.getTime(), persistedBookVO.getLaunchDate());
		assertEquals(BigDecimal.TEN, persistedBookVO.getPrice());
		assertEquals("Title Book", persistedBookVO.getTitle());
	}

	@Test
	@Order(3)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
		//mockBook();

		given()
			.spec(requestSpecification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("id", bookVO.getId())
				.when()
				.delete("{id}")
			.then()
				.statusCode(204);

	}

	@Test
	@Order(4)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {

		var content = given()
				.spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString()
		;

		List<BookVO> persistedBookVO = objectMapper.readValue(content, new TypeReference<List<BookVO>>(){});
		BookVO foundBookOneVO = persistedBookVO.get(0);

		assertNotNull(foundBookOneVO);

		assertNotNull(foundBookOneVO.getId());
		assertNotNull(foundBookOneVO.getAuthor());
		assertNotNull(foundBookOneVO.getLaunchDate());
		assertNotNull(foundBookOneVO.getPrice());
		assertNotNull(foundBookOneVO.getTitle());

		assertTrue(foundBookOneVO.getId() > 0);

		GregorianCalendar gc = new GregorianCalendar(2017, 10, 29);

		assertEquals("Michael C. Feathers", foundBookOneVO.getAuthor());
		assertEquals(gc.getTime(), foundBookOneVO.getLaunchDate());
		assertEquals(new BigDecimal(49).setScale(2), foundBookOneVO.getPrice().setScale(2));
		assertEquals("Working effectively with legacy code", foundBookOneVO.getTitle());
	}

	@Test
	@Order(5)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {

		RequestSpecification requestSpecificationWithoutToken = new RequestSpecBuilder()
				.setBasePath("/api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		given()
				.spec(requestSpecificationWithoutToken)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.when()
				.get()
				.then()
				.statusCode(401)
				;

	}

	private void mockBook() {
		//bookVO.setId(1L);
		bookVO.setAuthor("Author Book");
		GregorianCalendar gc = new GregorianCalendar(2023, 01, 27);
		bookVO.setLaunchDate(gc.getTime());
		bookVO.setPrice(BigDecimal.TEN);
		bookVO.setTitle("Title Book");
	}

}
