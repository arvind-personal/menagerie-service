package com.menagerie.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI menagerieOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Menagerie API")
                        .description("Pet and Event Management API")
                        .version("v3.0"));
    }
}
