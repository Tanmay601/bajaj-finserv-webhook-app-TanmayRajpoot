package com.bajaj.util;

import com.bajaj.dto.SolutionRequest;
import com.bajaj.dto.WebhookRequest;
import com.bajaj.dto.WebhookResponse;

public class TestDataBuilder {

    public static WebhookRequest createSampleWebhookRequest() {
        return new WebhookRequest("John Doe", "REG12347", "john@example.com");
    }

    public static WebhookRequest createWebhookRequestWithOddRegNo() {
        return new WebhookRequest("Jane Smith", "REG99999", "jane@example.com");
    }

    public static WebhookRequest createWebhookRequestWithEvenRegNo() {
        return new WebhookRequest("Bob Johnson", "REG10000", "bob@example.com");
    }

    public static WebhookResponse createSampleWebhookResponse() {
        return new WebhookResponse("https://example.com/webhook", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");
    }

    public static WebhookResponse createWebhookResponseWithCustomData(String webhook, String token) {
        return new WebhookResponse(webhook, token);
    }

    public static SolutionRequest createSampleSolutionRequest() {
        return new SolutionRequest("SELECT * FROM employees WHERE salary > 50000");
    }

    public static SolutionRequest createComplexSolutionRequest() {
        return new SolutionRequest("""
            SELECT 
                p.AMOUNT AS SALARY,
                CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME,
                TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE,
                d.DEPARTMENT_NAME
            FROM PAYMENTS p
            JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
            JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
            WHERE DAY(p.PAYMENT_TIME) != 1
            ORDER BY p.AMOUNT DESC
            LIMIT 1
            """);
    }

    public static String[] getTestRegistrationNumbers() {
        return new String[]{
            "REG12347", // Odd - Question 1
            "REG12346", // Even - Question 2
            "REG99999", // Odd - Question 1
            "REG10000", // Even - Question 2
            "REG00001", // Odd - Question 1
            "REG00000"  // Even - Question 2
        };
    }

    public static String getExpectedSqlContent() {
        return """
            SELECT 
                p.AMOUNT AS SALARY,
                CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME,
                TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE,
                d.DEPARTMENT_NAME
            FROM PAYMENTS p
            JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
            JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
            WHERE DAY(p.PAYMENT_TIME) != 1
            ORDER BY p.AMOUNT DESC
            LIMIT 1
            """;
    }
}
