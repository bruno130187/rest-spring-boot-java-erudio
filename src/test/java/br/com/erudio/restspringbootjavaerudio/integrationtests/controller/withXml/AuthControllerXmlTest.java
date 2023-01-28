package br.com.erudio.restspringbootjavaerudio.integrationtests.controller.withXml;

import br.com.erudio.restspringbootjavaerudio.configs.TestConfigs;
import br.com.erudio.restspringbootjavaerudio.data.vo.security.AccountCredentialsVO;
import br.com.erudio.restspringbootjavaerudio.data.vo.security.TokenVO;
import br.com.erudio.restspringbootjavaerudio.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource("classpath:testcontainers.properties")
public class AuthControllerXmlTest extends AbstractIntegrationTest {

	private static TokenVO tokenVO;
	private final String user = "leandro";
	private final String pass = "123456";
	@Test
	@Order(1)
	public void testSignin() throws JsonMappingException, JsonProcessingException {

		AccountCredentialsVO accountCredentialsVO = new AccountCredentialsVO(user, pass);

		tokenVO = given()
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_XML)
				.body(accountCredentialsVO)
					.when()
					.post()
				.then()
					.statusCode(200 )
					.extract()
					.body().as(TokenVO.class);

		assertNotNull(tokenVO.getAccessToken());
		assertNotNull(tokenVO.getRefreshToken());

	}
	@Test
	@Order(2)
	public void testRefresh() throws JsonMappingException, JsonProcessingException {

		AccountCredentialsVO accountCredentialsVO = new AccountCredentialsVO(user, pass);

		var newTokenVO = given()
				.basePath("/auth/refresh")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
					.pathParam("username", tokenVO.getUsername())
					.header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
				.when()
					.put("{username}")
				.then()
					.statusCode(200 )
				.extract()
				.body().as(TokenVO.class);

		assertNotNull(newTokenVO.getAccessToken());
		assertNotNull(newTokenVO.getRefreshToken());

	}

}
