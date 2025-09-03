package com.bajaj.integration;

import com.bajaj.dto.WebhookResponse;
import com.bajaj.service.BajajHealthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class BajajHealthServiceIntegrationTest {

    @Autowired
    private BajajHealthService bajajHealthService;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private WebClient webClient;

    @Test
    void testServiceBeanCreation() {
        assertNotNull(bajajHealthService);
    }

    @Test
    void testGenerateWebhookIntegration() {
        // Arrange
        WebhookResponse mockResponse = new WebhookResponse("https://test.com/webhook", "test-token");
        ResponseEntity<WebhookResponse> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        
        when(restTemplate.postForEntity(
            anyString(),
            any(),
            eq(WebhookResponse.class)
        )).thenReturn(responseEntity);

        // Act
        WebhookResponse result = bajajHealthService.generateWebhook();

        // Assert
        assertNotNull(result);
        assertEquals("https://test.com/webhook", result.getWebhook());
        assertEquals("test-token", result.getAccessToken());
    }

    @Test
    void testSolveSqlProblemIntegration() {
        // Test odd registration number
        String oddResult = bajajHealthService.solveSqlProblem("REG12347");
        assertNotNull(oddResult);
        assertTrue(oddResult.contains("SELECT"));
        assertTrue(oddResult.contains("PAYMENTS"));

        // Test even registration number
        String evenResult = bajajHealthService.solveSqlProblem("REG12346");
        assertNotNull(evenResult);
        assertTrue(evenResult.contains("SELECT"));
        assertTrue(evenResult.contains("PAYMENTS"));
    }

    @Test
    void testSqlSolutionContentIntegration() {
        // Test Question 1 content
        String question1Result = bajajHealthService.solveQuestion1();
        assertNotNull(question1Result);
        assertTrue(question1Result.contains("p.AMOUNT AS SALARY"));
        assertTrue(question1Result.contains("CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME"));
        assertTrue(question1Result.contains("TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE"));
        assertTrue(question1Result.contains("d.DEPARTMENT_NAME"));

        // Test Question 2 content
        String question2Result = bajajHealthService.solveQuestion2();
        assertNotNull(question2Result);
        assertTrue(question2Result.contains("p.AMOUNT AS SALARY"));
        assertTrue(question2Result.contains("CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME"));
        assertTrue(question2Result.contains("TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE"));
        assertTrue(question2Result.contains("d.DEPARTMENT_NAME"));
    }

    @Test
    void testSubmitSolutionIntegration() {
        // This test verifies the method can be called without throwing exceptions
        String accessToken = "test-token";
        String sqlSolution = "SELECT * FROM test_table";
        
        assertDoesNotThrow(() -> 
            bajajHealthService.submitSolution(accessToken, sqlSolution)
        );
    }

    @Test
    void testRegistrationNumberLogic() {
        // Test various registration number scenarios
        String[] testCases = {
            "REG12347", // Odd - should solve Question 1
            "REG12346", // Even - should solve Question 2
            "REG99999", // Odd - should solve Question 1
            "REG10000"  // Even - should solve Question 2
        };

        for (String regNo : testCases) {
            String result = bajajHealthService.solveSqlProblem(regNo);
            assertNotNull(result);
            assertTrue(result.contains("SELECT"));
            assertTrue(result.contains("PAYMENTS"));
            assertTrue(result.contains("EMPLOYEE"));
            assertTrue(result.contains("DEPARTMENT"));
        }
    }
}
