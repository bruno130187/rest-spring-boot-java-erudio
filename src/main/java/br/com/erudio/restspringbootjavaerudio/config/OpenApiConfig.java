package br.com.erudio.restspringbootjavaerudio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Bruno")
                        .version("V1")
                        .description("Api do Curso Rest Spring Boot Java Erudio")
                        .termsOfService("https://opensource.org/licenses/Apache-2.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://opensource.org/licenses/Apache-2.0")));
    }

}
