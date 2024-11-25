package com.example.userCRUD.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("UserCRUD API")
                        .version("0.0.1")
                        .description("API to manage users")
                        .termsOfService("https://policies.google.com/terms/archive/19990920?hl=en")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("API Support")
                                .url("https://google.com")
                                .email("contact@google.com"))
                        .license(new License()
                                .name("MIT License")
                        ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("DEV"))
                .addServersItem(new Server()
                        .url("https://www.google.com")
                        .description("Production"));
    }
}
