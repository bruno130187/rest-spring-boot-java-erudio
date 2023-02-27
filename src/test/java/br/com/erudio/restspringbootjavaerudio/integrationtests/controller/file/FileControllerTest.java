package br.com.erudio.restspringbootjavaerudio.integrationtests.controller.file;

import br.com.erudio.restspringbootjavaerudio.configs.TestConfigs;
import br.com.erudio.restspringbootjavaerudio.data.vo.security.AccountCredentialsVO;
import br.com.erudio.restspringbootjavaerudio.data.vo.security.TokenVO;
import br.com.erudio.restspringbootjavaerudio.data.vo.v1.UploadFileResponseVO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.File;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("classpath:testcontainers.properties")
public class FileControllerTest {

    private static RequestSpecification requestSpecification;
    private static UploadFileResponseVO uploadFileResponseVO;
    private final String user = "leandro";
    private final String pass = "123456";

    private final String uriFile1 = "D:\\Downloads\\project-.png";
    private final String uriFile2 = "D:\\Downloads\\oftware-development.jpg";
    private final String uriFile3 = "D:\\Downloads\\Mockaroo.PNG";

    @BeforeAll
    public static void setup() {

        uploadFileResponseVO = new UploadFileResponseVO();

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
    public void testUpload() throws JsonMappingException, JsonProcessingException {

        var content = given()
                .spec(requestSpecification)
                .basePath("/api/file/v1")
                .multiPart("file", new File(uriFile1), "image/png")
                .expect()
                .statusCode(200)
                .when()
                .post("/uploadFile");

    }

    @Test
    @Order(2)
    public void testUploads() throws JsonMappingException, JsonProcessingException {

        var content = given()
                .spec(requestSpecification)
                .basePath("/api/file/v1")
                .multiPart("files", new File(uriFile2), "image/jpg")
                .multiPart("files", new File(uriFile3), "image/png")
                .expect()
                .statusCode(200)
                .when()
                .post("/uploadMultipleFiles");

    }

    @Test
    @Order(3)
    public void testDownload() throws JsonMappingException, JsonProcessingException {

        var content = given()
                .spec(requestSpecification)
                .basePath("/api/file/v1")
                .expect()
                .statusCode(200)
                .when()
                .get("/downloadFile/project-.png");

    }

}
