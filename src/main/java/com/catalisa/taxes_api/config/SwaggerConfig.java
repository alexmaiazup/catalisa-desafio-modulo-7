package com.catalisa.taxes_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Taxes API")
                        .version("1.0.0")
                        .description("API para gerenciar tipos de impostos e calcular valores com base em alíquotas."));
    }
}