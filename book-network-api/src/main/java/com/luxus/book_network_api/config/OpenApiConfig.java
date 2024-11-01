package com.luxus.book_network_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Luxus Tempest",
                        email = "bahibmail@gmail.com" ,
                        url = ""
                                ),
                title = "Open API Specification - Luxus",
                version = "1.0",
                description = "API documentation for the Book Network application",
                license = @License(
                        name = "My License",
                        url = "http://www.tld.com"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        url = "http://localhost:8088/api/v1",
                        description = "Local server"
                ),
                @Server(
                        url = "https://production-domain-name.com",
                        description = "Production server"

                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)

@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Bearer Token auth ",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)

public class OpenApiConfig {
}
