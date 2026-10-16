package com.auth.auth_app_backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info =
        @Info(
            title = "Auth Application build by Ajit Kumar",
            description = "Generic auth app that can we used with any application",
            contact =
                @Contact(
                    name = "Ajit Kumar",
                    url = "http://example.com",
                    email = "example@gmail.com"),
            version = "1.0",
            summary = "This app is very useful if you don't want create auth app from scratch"),
    security = {@SecurityRequirement(name = "bearerAuth")})
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer", // Authorization: Bearer token
    bearerFormat = "JWT")
public class ApiDocConfig {}
