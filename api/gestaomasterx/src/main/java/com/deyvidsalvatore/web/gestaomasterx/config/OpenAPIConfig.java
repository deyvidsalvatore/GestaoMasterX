package com.deyvidsalvatore.web.gestaomasterx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {
	
	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Gestão Master X - Spring Boot 3 e Java 21")
						.version("1.0")
						.description("API de Gestão de Relacionamento de Funcionários com horas e feedback")
						.termsOfService("https://bit.ly/3LMGlRm")
						.license(
								new License()
								 .name("Apache 2.0")
								 .url("https://www.apache.org/licenses/LICENSE-2.0")
						)
				);
	}

}
