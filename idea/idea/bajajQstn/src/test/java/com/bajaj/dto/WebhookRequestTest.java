package com.bajaj.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WebhookRequestTest {

    @Test
    void testDefaultConstructor() {
        WebhookRequest request = new WebhookRequest();
        assertNotNull(request);
        assertNull(request.getName());
        assertNull(request.getRegNo());
        assertNull(request.getEmail());
    }

    @Test
    void testParameterizedConstructor() {
        String name = "John Doe";
        String regNo = "REG12347";
        String email = "john@example.com";

        WebhookRequest request = new WebhookRequest(name, regNo, email);

        assertEquals(name, request.getName());
        assertEquals(regNo, request.getRegNo());
        assertEquals(email, request.getEmail());
    }

    @Test
    void testSettersAndGetters() {
        WebhookRequest request = new WebhookRequest();

        String name = "Jane Smith";
        String regNo = "REG98765";
        String email = "jane@example.com";

        request.setName(name);
        request.setRegNo(regNo);
        request.setEmail(email);

        assertEquals(name, request.getName());
        assertEquals(regNo, request.getRegNo());
        assertEquals(email, request.getEmail());
    }

    @Test
    void testToString() {
        WebhookRequest request = new WebhookRequest("John Doe", "REG12347", "john@example.com");
        String result = request.toString();

        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("REG12347"));
        assertTrue(result.contains("john@example.com"));
        assertTrue(result.contains("WebhookRequest"));
    }

    @Test
    void testEqualsAndHashCode() {
        WebhookRequest request1 = new WebhookRequest("John Doe", "REG12347", "john@example.com");
        WebhookRequest request2 = new WebhookRequest("John Doe", "REG12347", "john@example.com");
        WebhookRequest request3 = new WebhookRequest("Jane Smith", "REG12347", "john@example.com");

        // Test equals
        assertEquals(request1.getName(), request2.getName());
        assertEquals(request1.getRegNo(), request2.getRegNo());
        assertEquals(request1.getEmail(), request2.getEmail());
        
        // Test different objects
        assertNotEquals(request1.getName(), request3.getName());
    }
}
