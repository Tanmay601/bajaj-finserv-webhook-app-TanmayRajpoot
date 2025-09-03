package com.bajaj.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WebhookResponseTest {

    @Test
    void testDefaultConstructor() {
        WebhookResponse response = new WebhookResponse();
        assertNotNull(response);
        assertNull(response.getWebhook());
        assertNull(response.getAccessToken());
    }

    @Test
    void testParameterizedConstructor() {
        String webhook = "https://example.com/webhook";
        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";

        WebhookResponse response = new WebhookResponse(webhook, accessToken);

        assertEquals(webhook, response.getWebhook());
        assertEquals(accessToken, response.getAccessToken());
    }

    @Test
    void testSettersAndGetters() {
        WebhookResponse response = new WebhookResponse();

        String webhook = "https://test.com/webhook";
        String accessToken = "test-token-123";

        response.setWebhook(webhook);
        response.setAccessToken(accessToken);

        assertEquals(webhook, response.getWebhook());
        assertEquals(accessToken, response.getAccessToken());
    }

    @Test
    void testToString() {
        WebhookResponse response = new WebhookResponse("https://example.com/webhook", "test-token");
        String result = response.toString();

        assertTrue(result.contains("https://example.com/webhook"));
        assertTrue(result.contains("test-token"));
        assertTrue(result.contains("WebhookResponse"));
    }

    @Test
    void testEqualsAndHashCode() {
        WebhookResponse response1 = new WebhookResponse("https://example.com/webhook", "token1");
        WebhookResponse response2 = new WebhookResponse("https://example.com/webhook", "token1");
        WebhookResponse response3 = new WebhookResponse("https://different.com/webhook", "token1");

        // Test equals
        assertEquals(response1.getWebhook(), response2.getWebhook());
        assertEquals(response1.getAccessToken(), response2.getAccessToken());
        
        // Test different objects
        assertNotEquals(response1.getWebhook(), response3.getWebhook());
    }
}
