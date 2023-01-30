package br.com.erudio.restspringbootjavaerudio.integrationtests.controller.withYaml;

import br.com.erudio.restspringbootjavaerudio.configs.TestConfigs;
import br.com.erudio.restspringbootjavaerudio.data.vo.security.AccountCredentialsVO;
import br.com.erudio.restspringbootjavaerudio.data.vo.security.TokenVO;
import br.com.erudio.restspringbootjavaerudio.integrationtests.controller.withYaml.mapper.YMLMapper;
import br.com.erudio.restspringbootjavaerudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.restspringbootjavaerudio.integrationtests.vo.PersonVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource("classpath:testcontainers.properties")
public class PersonControllerYamlTest extends AbstractIntegrationTest{
	private static RequestSpecification requestSpecification;
	private static YMLMapper ymlMapper;
	private static PersonVO personVO;
	private final String user = "leandro";
	private final String pass = "123456";
	@BeforeAll
	public static void setup() {
		ymlMapper = new YMLMapper();
		personVO = new PersonVO();
	}
	@Test
	@Order(0)
	public void testAuthorization() throws JsonMappingException, JsonProcessingException {

		AccountCredentialsVO accountCredentialsVO = new AccountCredentialsVO(user, pass);

		var accessToken = given()
				.config(
						RestAssuredConfig.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML,
												ContentType.TEXT)))
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_YML)
				.body(accountCredentialsVO, ymlMapper)
					.when()
					.post()
				.then()
					.statusCode(200 )
					.extract()
					.body().as(TokenVO.class, ymlMapper)
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

		var persistedPersonVO = given()
				.spec(requestSpecification)
				.config(
						RestAssuredConfig.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML,
												ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(personVO, ymlMapper)
				.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
							.body()
								.as(PersonVO.class, ymlMapper);
		

		personVO = persistedPersonVO;
		
		assertNotNull(persistedPersonVO);
		
		assertNotNull(persistedPersonVO.getId());
		assertNotNull(persistedPersonVO.getFirstName());
		assertNotNull(persistedPersonVO.getLastName());
		assertNotNull(persistedPersonVO.getAddress());
		assertNotNull(persistedPersonVO.getGender());
		assertTrue(persistedPersonVO.getEnabled());
		
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

		var persistedPersonVO = given()
				.spec(requestSpecification)
				.config(
						RestAssuredConfig.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML,
												ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(personVO, ymlMapper)
					.when()
					.post()
				.then()
				.statusCode(200)
					.extract()
				.body()
				.as(PersonVO.class, ymlMapper);

		personVO = persistedPersonVO;

		assertNotNull(persistedPersonVO);

		assertNotNull(persistedPersonVO.getId());
		assertNotNull(persistedPersonVO.getFirstName());
		assertNotNull(persistedPersonVO.getLastName());
		assertNotNull(persistedPersonVO.getAddress());
		assertNotNull(persistedPersonVO.getGender());
		assertTrue(persistedPersonVO.getEnabled());

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

		var persistedPersonVO = given()
				.spec(requestSpecification)
				.config(
						RestAssuredConfig.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML,
												ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("id", personVO.getId())
				.when()
				.get("{id}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(PersonVO.class, ymlMapper);

		personVO = persistedPersonVO;

		assertNotNull(personVO);

		assertNotNull(personVO.getId());
		assertNotNull(personVO.getFirstName());
		assertNotNull(personVO.getLastName());
		assertNotNull(personVO.getAddress());
		assertNotNull(personVO.getGender());
		assertTrue(personVO.getEnabled());

		assertEquals(personVO.getId(), personVO.getId());

		assertEquals("Nelson", personVO.getFirstName());
		assertEquals("Senna", personVO.getLastName());
		assertEquals("São Paulo, SP, BR", personVO.getAddress());
		assertEquals("Male", personVO.getGender());
	}

	@Test
	@Order(4)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {

		var content = given()
				.spec(requestSpecification)
				.config(
						RestAssuredConfig.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML,
												ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(PersonVO[].class, ymlMapper);
		;

		List<PersonVO> persistedPersonVO = Arrays.asList(content);
		PersonVO foundPersonOneVO = persistedPersonVO.get(0);

		assertNotNull(foundPersonOneVO);

		assertNotNull(foundPersonOneVO.getId());
		assertNotNull(foundPersonOneVO.getFirstName());
		assertNotNull(foundPersonOneVO.getLastName());
		assertNotNull(foundPersonOneVO.getAddress());
		assertNotNull(foundPersonOneVO.getGender());
		assertTrue(foundPersonOneVO.getEnabled());

		assertTrue(foundPersonOneVO.getId() > 0);

		assertEquals("Bruno", foundPersonOneVO.getFirstName());
		assertEquals("Araújo", foundPersonOneVO.getLastName());
		assertEquals("Endereço 3", foundPersonOneVO.getAddress());
		assertEquals("Male", foundPersonOneVO.getGender());
	}

	@Test
	@Order(5)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {

		RequestSpecification requestSpecificationWithoutToken = new RequestSpecBuilder()
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		given()
				.spec(requestSpecificationWithoutToken)
				.config(
						RestAssuredConfig.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML,
												ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.when()
				.get()
				.then()
				.statusCode(401)
				;

	}

	@Test
	@Order(6)
	public void testDisablePersonById() throws JsonMappingException, JsonProcessingException {

		var persistedPersonVO = given()
				.spec(requestSpecification)
				.config(
						RestAssuredConfig.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML,
												ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("id", personVO.getId())
				.pathParam("enabled", 0)
				.when()
				.patch("{id}/{enabled}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(PersonVO.class, ymlMapper);

		personVO = persistedPersonVO;

		assertNotNull(personVO);

		assertNotNull(personVO.getId());
		assertNotNull(personVO.getFirstName());
		assertNotNull(personVO.getLastName());
		assertNotNull(personVO.getAddress());
		assertNotNull(personVO.getGender());
		assertFalse(personVO.getEnabled());

		assertEquals(personVO.getId(), personVO.getId());

		assertEquals("Nelson", personVO.getFirstName());
		assertEquals("Senna", personVO.getLastName());
		assertEquals("São Paulo, SP, BR", personVO.getAddress());
		assertEquals("Male", personVO.getGender());
	}

	@Test
	@Order(7)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
		//mockPerson();

		given()
				.spec(requestSpecification)
				.config(
						RestAssuredConfig.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML,
												ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("id", personVO.getId())
				.when()
				.delete("{id}")
				.then()
				.statusCode(204);

	}

	private static void mockPerson() {
		personVO.setId(1L);
		personVO.setFirstName("Nelson");
		personVO.setLastName("Piquet");
		personVO.setAddress("São Paulo, SP, BR");
		personVO.setGender("Male");
		personVO.setEnabled(true);
	}

}
