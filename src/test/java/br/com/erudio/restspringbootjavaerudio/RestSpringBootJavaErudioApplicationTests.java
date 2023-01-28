package br.com.erudio.restspringbootjavaerudio;

import br.com.erudio.restspringbootjavaerudio.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("classpath:testcontainers.properties")
class RestSpringBootJavaErudioApplicationTests extends AbstractIntegrationTest {

	@Test
	void contextLoads() {
	}

}
