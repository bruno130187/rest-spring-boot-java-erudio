package br.com.erudio.restspringbootjavaerudio.integrationtests.controller.withJson;

import br.com.erudio.restspringbootjavaerudio.configs.TestConfigs;
import br.com.erudio.restspringbootjavaerudio.data.vo.security.AccountCredentialsVO;
import br.com.erudio.restspringbootjavaerudio.data.vo.security.TokenVO;
import br.com.erudio.restspringbootjavaerudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.restspringbootjavaerudio.integrationtests.vo.PersonVO;
import br.com.erudio.restspringbootjavaerudio.integrationtests.vo.wrapper.WrapperPersonVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource("classpath:testcontainers.properties")
public class PersonControllerJsonTest extends AbstractIntegrationTest{
	private static RequestSpecification requestSpecification;
	private static ObjectMapper objectMapper;
	private static PersonVO personVO;
	private final String user = "leandro";
	private final String pass = "123456";
	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		personVO = new PersonVO();
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
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

	}
	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		mockPerson();
		
		var content = given()
				.spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(personVO)
				.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
							.body()
								.asString();
		
		PersonVO persistedPersonVO = objectMapper.readValue(content, PersonVO.class);
		personVO = persistedPersonVO;
		
		assertNotNull(persistedPersonVO);
		
		assertNotNull(persistedPersonVO.getId());
		assertNotNull(persistedPersonVO.getFirstName());
		assertNotNull(persistedPersonVO.getLastName());
		assertNotNull(persistedPersonVO.getAddress());
		assertNotNull(persistedPersonVO.getGender());
		assertTrue(persistedPersonVO.getEnabled());
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8090/api/person/v1/1007\""));

		
		assertTrue(persistedPersonVO.getId() > 0);

		assertEquals("Nelson", persistedPersonVO.getFirstName());
		assertEquals("Piquet", persistedPersonVO.getLastName());
		assertEquals("São Paulo, SP, BR", persistedPersonVO.getAddress());
		assertEquals("Male", persistedPersonVO.getGender());
	}

	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		personVO.setLastName("Senna");

		var content = given()
				.spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.body(personVO)
					.when()
					.post()
				.then()
				.statusCode(200)
					.extract()
					.body()
						.asString();

		PersonVO persistedPersonVO = objectMapper.readValue(content, PersonVO.class);
		personVO = persistedPersonVO;

		assertNotNull(persistedPersonVO);

		assertNotNull(persistedPersonVO.getId());
		assertNotNull(persistedPersonVO.getFirstName());
		assertNotNull(persistedPersonVO.getLastName());
		assertNotNull(persistedPersonVO.getAddress());
		assertNotNull(persistedPersonVO.getGender());
		assertTrue(persistedPersonVO.getEnabled());
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8090/api/person/v1/1007\""));

		assertEquals(persistedPersonVO.getId(), personVO.getId());

		assertTrue(persistedPersonVO.getId() > 0);

		assertEquals("Nelson", persistedPersonVO.getFirstName());
		assertEquals("Senna", persistedPersonVO.getLastName());
		assertEquals("São Paulo, SP, BR", persistedPersonVO.getAddress());
		assertEquals("Male", persistedPersonVO.getGender());
	}
	@Test
	@Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		//mockPerson();

		var content = given().spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("id", personVO.getId())
				.when()
				.get("{id}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		personVO = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertTrue(persistedPerson.getEnabled());
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8090/api/person/v1/1007\""));

		assertEquals(personVO.getId(), persistedPerson.getId());

		assertEquals("Nelson", persistedPerson.getFirstName());
		assertEquals("Senna", persistedPerson.getLastName());
		assertEquals("São Paulo, SP, BR", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}

	@Test
	@Order(4)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {

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
				//.as(new TypeRef<List<PersonVO>>() {})
				.asString()
		;

		WrapperPersonVO wrapperPersonVO = objectMapper.readValue(content, WrapperPersonVO.class);
		var people = wrapperPersonVO.getEmbedded().getPersons();
		PersonVO foundPersonOneVO = people.get(0);

		assertNotNull(foundPersonOneVO);

		assertNotNull(foundPersonOneVO.getId());
		assertNotNull(foundPersonOneVO.getFirstName());
		assertNotNull(foundPersonOneVO.getLastName());
		assertNotNull(foundPersonOneVO.getAddress());
		assertNotNull(foundPersonOneVO.getGender());
		assertFalse(foundPersonOneVO.getEnabled());
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8090/api/person/v1/32\""));

		assertTrue(foundPersonOneVO.getId() > 0);

		assertEquals("Allister", foundPersonOneVO.getFirstName());
		assertEquals("McLaughlan", foundPersonOneVO.getLastName());
		assertEquals("526 Tomscot Parkway", foundPersonOneVO.getAddress());
		assertEquals("Male", foundPersonOneVO.getGender());
	}

	@Test
	@Order(5)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {

		var content = given()
				.spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("firstName", "and")
				.queryParams("page", 0, "size", 10, "direction", "asc")
				.when()
				.get("findPeopleByName/{firstName}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString()
				;

		WrapperPersonVO wrapperPersonVO = objectMapper.readValue(content, WrapperPersonVO.class);
		var people = wrapperPersonVO.getEmbedded().getPersons();
		PersonVO foundPersonOneVO = people.get(0);

		assertNotNull(foundPersonOneVO);

		assertNotNull(foundPersonOneVO.getId());
		assertNotNull(foundPersonOneVO.getFirstName());
		assertNotNull(foundPersonOneVO.getLastName());
		assertNotNull(foundPersonOneVO.getAddress());
		assertNotNull(foundPersonOneVO.getGender());
		assertTrue(foundPersonOneVO.getEnabled());
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8090/api/person/v1/8\""));

		assertTrue(foundPersonOneVO.getId() > 0);

		assertEquals("Wayland", foundPersonOneVO.getFirstName());
		assertEquals("Thamelt", foundPersonOneVO.getLastName());
		assertEquals("337 Everett Park", foundPersonOneVO.getAddress());
		assertEquals("Male", foundPersonOneVO.getGender());
	}

	@Test
	@Order(6)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {

		RequestSpecification requestSpecificationWithoutToken = new RequestSpecBuilder()
				.setBasePath("/api/person/v1")
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
	public void testDisablePersonById() throws JsonMappingException, JsonProcessingException {

		var content = given().spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("id", personVO.getId())
				.pathParam("enabled", 0)
				.when()
				.patch("{id}/{enabled}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		personVO = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertFalse(persistedPerson.getEnabled());
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8090/api/person/v1/1007\""));

		assertEquals(personVO.getId(), persistedPerson.getId());

		assertEquals("Nelson", persistedPerson.getFirstName());
		assertEquals("Senna", persistedPerson.getLastName());
		assertEquals("São Paulo, SP, BR", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}

	@Test
	@Order(8)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
		//mockPerson();

		given()
				.spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("id", personVO.getId())
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

		assertTrue(content.contains("\"_links\":{\"first\":{\"href\":\"http://localhost:8090/api/person/v1?direction=asc&page=0&size=10&sort=id,asc\"}"));
		assertTrue(content.contains("\"prev\":{\"href\":\"http://localhost:8090/api/person/v1?direction=asc&page=2&size=10&sort=id,asc\"}"));
		assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8090/api/person/v1?page=3&size=3&direction=asc\"}"));
		assertTrue(content.contains("\"next\":{\"href\":\"http://localhost:8090/api/person/v1?direction=asc&page=4&size=10&sort=id,asc\"}"));
		assertTrue(content.contains("\"last\":{\"href\":\"http://localhost:8090/api/person/v1?direction=asc&page=100&size=10&sort=id,asc\"}"));
		assertTrue(content.contains("\"page\":{\"size\":10,\"totalElements\":1005,\"totalPages\":101,\"number\":3}"));

	}

	private void mockPerson() {
		//personVO.setId(1L);
		personVO.setFirstName("Nelson");
		personVO.setLastName("Piquet");
		personVO.setAddress("São Paulo, SP, BR");
		personVO.setGender("Male");
		personVO.setEnabled(true);
	}

}
