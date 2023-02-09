package br.com.erudio.restspringbootjavaerudio.integrationtests.controller.withXml;

import br.com.erudio.restspringbootjavaerudio.configs.TestConfigs;
import br.com.erudio.restspringbootjavaerudio.data.vo.security.AccountCredentialsVO;
import br.com.erudio.restspringbootjavaerudio.data.vo.security.TokenVO;
import br.com.erudio.restspringbootjavaerudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.restspringbootjavaerudio.integrationtests.vo.BookVO;
import br.com.erudio.restspringbootjavaerudio.integrationtests.vo.pagedModels.PagedModelBook;
import br.com.erudio.restspringbootjavaerudio.integrationtests.vo.pagedModels.PagedModelPerson;
import br.com.erudio.restspringbootjavaerudio.integrationtests.vo.wrapper.WrapperBookVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource("classpath:testcontainers.properties")
public class BookControllerXmlTest extends AbstractIntegrationTest{
	private static RequestSpecification requestSpecification;
	private static XmlMapper xmlMapper;
	private static BookVO bookVO;
	private final String user = "leandro";
	private final String pass = "123456";
	@BeforeAll
	public static void setup() {
		xmlMapper = new XmlMapper();
		xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		bookVO = new BookVO();
	}
	@Test
	@Order(0)
	public void testAuthorization() throws JsonMappingException, JsonProcessingException {

		AccountCredentialsVO accountCredentialsVO = new AccountCredentialsVO(user, pass);

		var accessToken = given()
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_XML)
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

		var content = given().spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.body(bookVO)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		BookVO persistedBookVO = xmlMapper.readValue(content, BookVO.class);
		bookVO = persistedBookVO;
		
		assertNotNull(persistedBookVO);
		
		assertNotNull(persistedBookVO.getId());
		assertNotNull(persistedBookVO.getAuthor());
		assertNotNull(persistedBookVO.getLaunchDate());
		assertNotNull(persistedBookVO.getPrice());
		assertNotNull(persistedBookVO.getTitle());
		assertTrue(persistedBookVO.getEnabled());
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8090/api/book/v1/1</href></links>"));
		
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
		bookVO.setAuthor("Author Book Updated 2");

		var content = given()
				.spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
					.body(bookVO)
					.when()
					.post()
				.then()
				.statusCode(200)
					.extract()
					.body()
						.asString();

		BookVO persistedBookVO = xmlMapper.readValue(content, BookVO.class);
		bookVO = persistedBookVO;

		assertNotNull(persistedBookVO);

		assertNotNull(persistedBookVO.getId());
		assertNotNull(persistedBookVO.getAuthor());
		assertNotNull(persistedBookVO.getLaunchDate());
		assertNotNull(persistedBookVO.getPrice());
		assertNotNull(persistedBookVO.getTitle());
		assertTrue(persistedBookVO.getEnabled());
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8090/api/book/v1/1</href></links>"));

		assertEquals(persistedBookVO.getId(), bookVO.getId());

		assertTrue(persistedBookVO.getId() > 0);

		GregorianCalendar gc = new GregorianCalendar(2023, 01, 27);

		assertEquals("Author Book Updated 2", persistedBookVO.getAuthor());
		assertEquals(gc.getTime(), persistedBookVO.getLaunchDate());
		assertEquals(BigDecimal.TEN, persistedBookVO.getPrice());
		assertEquals("Title Book", persistedBookVO.getTitle());
	}

	@Test
	@Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		//mockBook();

		var content = given().spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.pathParam("id", bookVO.getId())
				.when()
				.get("{id}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		BookVO persistedBookVO = xmlMapper.readValue(content, BookVO.class);
		bookVO = persistedBookVO;

		assertNotNull(persistedBookVO);

		assertNotNull(persistedBookVO.getId());
		assertNotNull(persistedBookVO.getId());
		assertNotNull(persistedBookVO.getAuthor());
		assertNotNull(persistedBookVO.getLaunchDate());
		assertNotNull(persistedBookVO.getPrice());
		assertNotNull(persistedBookVO.getTitle());
		assertTrue(persistedBookVO.getEnabled());
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8090/api/book/v1/1</href></links>"));

		assertEquals(bookVO.getId(), persistedBookVO.getId());

		assertTrue(persistedBookVO.getId() > 0);

		GregorianCalendar gc = new GregorianCalendar(2023, 01, 27);

		assertEquals("Author Book Updated 2", persistedBookVO.getAuthor());
		assertEquals(gc.getTime(), persistedBookVO.getLaunchDate());
		assertEquals(new BigDecimal(10).setScale(2), persistedBookVO.getPrice().setScale(2));
		assertEquals("Title Book", persistedBookVO.getTitle());
	}

	@Test
	@Order(4)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {

		var content = given()
				.spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.queryParams("page", 0, "size", 10, "direction", "asc")
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString()
		;

		PagedModelBook pagedModelBook = xmlMapper.readValue(content, PagedModelBook.class);
		var books = pagedModelBook.getContent();
		BookVO foundBookOneVO = books.get(0);

		assertNotNull(foundBookOneVO);

		assertNotNull(foundBookOneVO.getId());
		assertNotNull(foundBookOneVO.getAuthor());
		assertNotNull(foundBookOneVO.getLaunchDate());
		assertNotNull(foundBookOneVO.getPrice());
		assertNotNull(foundBookOneVO.getTitle());
		assertTrue(foundBookOneVO.getEnabled());
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8090/api/book/v1/1</href></links>"));

		assertTrue(foundBookOneVO.getId() > 0);

		GregorianCalendar gc = new GregorianCalendar(2023, 01, 27);

		assertEquals("Author Book Updated 2", foundBookOneVO.getAuthor());
		assertEquals(gc.getTime(), foundBookOneVO.getLaunchDate());
		assertEquals(new BigDecimal(10).setScale(2), foundBookOneVO.getPrice().setScale(2));
		assertEquals("Title Book", foundBookOneVO.getTitle());
	}

	@Test
	@Order(5)
	public void testFindByTitle() throws JsonMappingException, JsonProcessingException {
		//mockBook();

		var content = given().spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.pathParam("title", "code")
				.queryParams("page", 0, "size", 10, "direction", "asc")
				.when()
				.get("findBooksByTitle/{title}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		PagedModelBook pagedModelBook = xmlMapper.readValue(content, PagedModelBook.class);
		var books = pagedModelBook.getContent();
		BookVO foundBookOneVO = books.get(0);

		assertNotNull(foundBookOneVO);

		assertNotNull(foundBookOneVO.getId());
		assertNotNull(foundBookOneVO.getAuthor());
		assertNotNull(foundBookOneVO.getLaunchDate());
		assertNotNull(foundBookOneVO.getPrice());
		assertNotNull(foundBookOneVO.getTitle());
		assertTrue(foundBookOneVO.getEnabled());
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8090/api/book/v1/3</href></links>"));

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
				.contentType(TestConfigs.CONTENT_TYPE_XML)
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
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.pathParam("id", bookVO.getId())
				.pathParam("enabled", 0)
				.when()
				.patch("{id}/{enabled}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		BookVO persistedBookVO = xmlMapper.readValue(content, BookVO.class);
		bookVO = persistedBookVO;

		assertNotNull(persistedBookVO);

		assertNotNull(persistedBookVO.getId());
		assertNotNull(persistedBookVO.getAuthor());
		assertNotNull(persistedBookVO.getLaunchDate());
		assertNotNull(persistedBookVO.getPrice());
		assertNotNull(persistedBookVO.getTitle());
		assertFalse(persistedBookVO.getEnabled());
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8090/api/book/v1/1</href></links>"));

		assertEquals(bookVO.getId(), persistedBookVO.getId());

		GregorianCalendar gc = new GregorianCalendar(2023, 01, 27);

		assertEquals("Author Book Updated 2", persistedBookVO.getAuthor());
		assertEquals(gc.getTime(), persistedBookVO.getLaunchDate());
		assertEquals(new BigDecimal(10).setScale(2), persistedBookVO.getPrice().setScale(2));
		assertEquals("Title Book", persistedBookVO.getTitle());
	}

	@Test
	@Order(8)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
		//mockBook();

		given()
				.spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
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
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.queryParams("page", 0, "size", 10, "direction", "asc")
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		assertTrue(content.contains("<links><rel>first</rel><href>http://localhost:8090/api/book/v1?direction=asc&amp;page=0&amp;size=10&amp;sort=id,asc</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8090/api/book/v1?page=0&amp;size=0&amp;direction=asc</href></links>"));
		assertTrue(content.contains("<links><rel>next</rel><href>http://localhost:8090/api/book/v1?direction=asc&amp;page=1&amp;size=10&amp;sort=id,asc</href></links>"));
		assertTrue(content.contains("<links><rel>last</rel><href>http://localhost:8090/api/book/v1?direction=asc&amp;page=1&amp;size=10&amp;sort=id,asc</href></links>"));
		assertTrue(content.contains("<page><size>10</size><totalElements>14</totalElements><totalPages>2</totalPages><number>0</number></page>"));

	}

	private void mockBook() {
		bookVO.setId(1L);
		bookVO.setAuthor("Author Book");
		GregorianCalendar gc = new GregorianCalendar(2023, 01, 27);
		bookVO.setLaunchDate(gc.getTime());
		bookVO.setPrice(BigDecimal.TEN);
		bookVO.setTitle("Title Book");
		bookVO.setEnabled(true);
	}

}
