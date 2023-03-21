package com.guardjo.simpleblogfinder.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class CommonConfig {
    private final static String KAKAO_REST_API_HEADER_PREFIX = "KakaoAK";

    @Value("${KAKAO_REST_API_KEY}")
    private String KAKAO_REST_API_KEY;
    @Bean
    @Qualifier("kakaoWebClient")
    public WebClient webClient() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, KAKAO_REST_API_HEADER_PREFIX + " " + KAKAO_REST_API_KEY)
                .build();
    }
}
