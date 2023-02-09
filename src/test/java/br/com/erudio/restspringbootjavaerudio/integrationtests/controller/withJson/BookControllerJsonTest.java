package br.com.erudio.restspringbootjavaerudio.integrationtests.controller.withJson;

import br.com.erudio.restspringbootjavaerudio.configs.TestConfigs;
import br.com.erudio.restspringbootjavaerudio.data.vo.security.AccountCredentialsVO;
import br.com.erudio.restspringbootjavaerudio.data.vo.security.TokenVO;
import br.com.erudio.restspringbootjavaerudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.restspringbootjavaerudio.integrationtests.vo.BookVO;
import br.com.erudio.restspringbootjavaerudio.integrationtests.vo.PersonVO;
import br.com.erudio.restspringbootjavaerudio.integrationtests.vo.wrapper.WrapperBookVO;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

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
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8090/api/book/v1/16\""));
		
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
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8090/api/book/v1/16\""));

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
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		//mockBook();

		var content = given().spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("id", bookVO.getId())
				.when()
				.get("{id}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
		bookVO = persistedBook;

		assertNotNull(persistedBook);

		assertNotNull(persistedBook.getId());
		assertNotNull(persistedBook.getId());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getLaunchDate());
		assertNotNull(persistedBook.getPrice());
		assertNotNull(persistedBook.getTitle());
		assertTrue(persistedBook.getEnabled());
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8090/api/book/v1/16\""));

		assertEquals(bookVO.getId(), persistedBook.getId());

		assertTrue(persistedBook.getId() > 0);

		GregorianCalendar gc = new GregorianCalendar(2023, 01, 27);

		assertEquals("Author Book Updated", persistedBook.getAuthor());
		assertEquals(gc.getTime(), persistedBook.getLaunchDate());
		assertEquals(new BigDecimal(10).setScale(2), persistedBook.getPrice().setScale(2));
		assertEquals("Title Book", persistedBook.getTitle());
	}

	@Test
	@Order(4)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {

		var content = given()
				.spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.queryParams("page", 0, "size", 10, "direction", "asc")
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString()
		;

		WrapperBookVO wrapperBookVO = objectMapper.readValue(content, WrapperBookVO.class);
		var books = wrapperBookVO.getEmbedded().getBooks();
		BookVO foundBookOneVO = books.get(0);

		assertNotNull(foundBookOneVO);

		assertNotNull(foundBookOneVO.getId());
		assertNotNull(foundBookOneVO.getAuthor());
		assertNotNull(foundBookOneVO.getLaunchDate());
		assertNotNull(foundBookOneVO.getPrice());
		assertNotNull(foundBookOneVO.getTitle());
		assertTrue(foundBookOneVO.getEnabled());
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8090/api/book/v1/1\""));

		assertTrue(foundBookOneVO.getId() > 0);

		GregorianCalendar gc = new GregorianCalendar(2017, 10, 29);

		assertEquals("Michael C. Feathers", foundBookOneVO.getAuthor());
		assertEquals(gc.getTime(), foundBookOneVO.getLaunchDate());
		assertEquals(new BigDecimal(49).setScale(2), foundBookOneVO.getPrice().setScale(2));
		assertEquals("Working effectively with legacy code", foundBookOneVO.getTitle());
	}

	@Test
	@Order(5)
	public void testFindByTitle() throws JsonMappingException, JsonProcessingException {
		//mockBook();

		var content = given().spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("title", "code")
				.queryParams("page", 0, "size", 10, "direction", "asc")
				.when()
				.get("findBooksByTitle/{title}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		WrapperBookVO wrapperBookVO = objectMapper.readValue(content, WrapperBookVO.class);
		var books = wrapperBookVO.getEmbedded().getBooks();
		BookVO foundBookOneVO = books.get(0);

		assertNotNull(foundBookOneVO);

		assertNotNull(foundBookOneVO.getId());
		assertNotNull(foundBookOneVO.getAuthor());
		assertNotNull(foundBookOneVO.getLaunchDate());
		assertNotNull(foundBookOneVO.getPrice());
		assertNotNull(foundBookOneVO.getTitle());
		assertTrue(foundBookOneVO.getEnabled());
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8090/api/book/v1/1\""));

		assertTrue(foundBookOneVO.getId() > 0);

		GregorianCalendar gc = new GregorianCalendar(2009, 00, 10);

		assertEquals("Robert C. Martin", foundBookOneVO.getAuthor());
		assertEquals(gc.getTime(), foundBookOneVO.getLaunchDate());
		assertEquals(new BigDecimal(77).setScale(2), foundBookOneVO.getPrice().setScale(2));
		assertEquals("Clean Code", foundBookOneVO.getTitle());
	}

	@Test
	@Order(6)
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

	@Test
	@Order(7)
	public void testDisableBookById() throws JsonMappingException, JsonProcessingException {

		var content = given().spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("id", bookVO.getId())
				.pathParam("enabled", 0)
				.when()
				.patch("{id}/{enabled}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
		bookVO = persistedBook;

		assertNotNull(persistedBook);

		assertNotNull(persistedBook.getId());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getLaunchDate());
		assertNotNull(persistedBook.getPrice());
		assertNotNull(persistedBook.getTitle());
		assertFalse(persistedBook.getEnabled());
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8090/api/book/v1/16\""));

		assertEquals(bookVO.getId(), persistedBook.getId());

		GregorianCalendar gc = new GregorianCalendar(2023, 01, 27);

		assertEquals("Author Book Updated", persistedBook.getAuthor());
		assertEquals(gc.getTime(), persistedBook.getLaunchDate());
		assertEquals(new BigDecimal(10).setScale(2), persistedBook.getPrice().setScale(2));
		assertEquals("Title Book", persistedBook.getTitle());
	}

	@Test
	@Order(8)
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
	@Order(9)
	public void testHATEOAS() throws JsonMappingException, JsonProcessingException {

		var content = given()
				.spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.queryParams("page", 3, "size", 10, "direction", "asc")
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		assertTrue(content.contains("\"_links\":{\"first\":{\"href\":\"http://localhost:8090/api/book/v1?direction=asc&page=0&size=10&sort=id,asc\"}"));
		assertTrue(content.contains("\"prev\":{\"href\":\"http://localhost:8090/api/book/v1?direction=asc&page=2&size=10&sort=id,asc\"}"));
		assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8090/api/book/v1?page=3&size=3&direction=asc\"}"));
		assertTrue(content.contains("\"last\":{\"href\":\"http://localhost:8090/api/book/v1?direction=asc&page=1&size=10&sort=id,asc\"}"));
		assertTrue(content.contains("\"page\":{\"size\":10,\"totalElements\":15,\"totalPages\":2,\"number\":3}"));

	}

	private void mockBook() {
		//bookVO.setId(1L);
		bookVO.setAuthor("Author Book");
		GregorianCalendar gc = new GregorianCalendar(2023, 01, 27);
		bookVO.setLaunchDate(gc.getTime());
		bookVO.setPrice(BigDecimal.TEN);
		bookVO.setTitle("Title Book");
		bookVO.setEnabled(true);
	}

}
