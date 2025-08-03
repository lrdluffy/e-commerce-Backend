package com.strawhats.ecommercebackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Strawhats E-Commerce Backend")
                                .description("Strawhats E-Commerce Backend")
                                .version("1.0")
                );
    }
}
