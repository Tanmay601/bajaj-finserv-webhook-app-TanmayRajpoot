package com.bajaj.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.main.web-application-type=servlet"
})
class WebConfigTest {

    private final WebConfig webConfig = new WebConfig();

    @Test
    void testRestTemplateBean() {
        RestTemplate restTemplate = webConfig.restTemplate();
        
        assertNotNull(restTemplate);
        assertTrue(restTemplate instanceof RestTemplate);
    }

    @Test
    void testWebClientBean() {
        WebClient webClient = webConfig.webClient();
        
        assertNotNull(webClient);
        assertTrue(webClient instanceof WebClient);
    }

    @Test
    void testRestTemplateNotNull() {
        RestTemplate restTemplate = webConfig.restTemplate();
        assertNotNull(restTemplate);
    }

    @Test
    void testWebClientNotNull() {
        WebClient webClient = webConfig.webClient();
        assertNotNull(webClient);
    }
}
