package com.itau.localizador_pets.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Localizacão API")
                        .description("Documentação da API do sistema de localização de pets.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Equipe de Desenvolvimento")
                                .email("arianarusso@hotmail.com.br"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
