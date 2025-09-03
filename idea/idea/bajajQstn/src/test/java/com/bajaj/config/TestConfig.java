package com.bajaj.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public RestTemplate testRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Primary
    public WebClient testWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }
}
