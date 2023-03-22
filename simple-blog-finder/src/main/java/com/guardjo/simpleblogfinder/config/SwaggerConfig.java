package com.guardjo.simpleblogfinder.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("블로그 검색 도우미")
                .version("v1.0.0")
                .description("검색어를 기반으로 카카오 블로그 정보를 조회한다");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
