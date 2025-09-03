package com.bajaj.service;

import com.bajaj.dto.SolutionRequest;
import com.bajaj.dto.WebhookRequest;
import com.bajaj.dto.WebhookResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BajajHealthServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private BajajHealthService bajajHealthService;

    private WebhookResponse mockWebhookResponse;
    private WebhookRequest expectedWebhookRequest;

    @BeforeEach
    void setUp() {
        mockWebhookResponse = new WebhookResponse("https://example.com/webhook", "test-jwt-token");
        expectedWebhookRequest = new WebhookRequest("John Doe", "REG12347", "john@example.com");
    }

    @Test
    void testGenerateWebhook_Success() {
        // Arrange
        ResponseEntity<WebhookResponse> mockResponse = new ResponseEntity<>(mockWebhookResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(
            eq("https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA"),
            any(HttpEntity.class),
            eq(WebhookResponse.class)
        )).thenReturn(mockResponse);

        // Act
        WebhookResponse result = bajajHealthService.generateWebhook();

        // Assert
        assertNotNull(result);
        assertEquals("https://example.com/webhook", result.getWebhook());
        assertEquals("test-jwt-token", result.getAccessToken());
        
        verify(restTemplate).postForEntity(
            eq("https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA"),
            any(HttpEntity.class),
            eq(WebhookResponse.class)
        );
    }

    @Test
    void testGenerateWebhook_Failure() {
        // Arrange
        ResponseEntity<WebhookResponse> mockResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.postForEntity(
            anyString(),
            any(HttpEntity.class),
            eq(WebhookResponse.class)
        )).thenReturn(mockResponse);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> bajajHealthService.generateWebhook());
    }

    @Test
    void testSolveSqlProblem_OddRegistrationNumber() {
        // Act
        String result = bajajHealthService.solveSqlProblem("REG12347");

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("SELECT"));
        assertTrue(result.contains("PAYMENTS"));
        assertTrue(result.contains("EMPLOYEE"));
        assertTrue(result.contains("DEPARTMENT"));
        assertTrue(result.contains("DAY(p.PAYMENT_TIME) != 1"));
    }

    @Test
    void testSolveSqlProblem_EvenRegistrationNumber() {
        // Act
        String result = bajajHealthService.solveSqlProblem("REG12346");

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("SELECT"));
        assertTrue(result.contains("PAYMENTS"));
        assertTrue(result.contains("EMPLOYEE"));
        assertTrue(result.contains("DEPARTMENT"));
        assertTrue(result.contains("DAY(p.PAYMENT_TIME) != 1"));
    }

    @Test
    void testSolveSqlProblem_RegistrationNumberEndingWith00() {
        // Act
        String result = bajajHealthService.solveSqlProblem("REG12300");

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("SELECT"));
        // Should solve Question 2 (even)
    }

    @Test
    void testSolveSqlProblem_RegistrationNumberEndingWith99() {
        // Act
        String result = bajajHealthService.solveSqlProblem("REG12399");

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("SELECT"));
        // Should solve Question 1 (odd)
    }

    @Test
    void testSubmitSolution_Success() {
        // Arrange
        String accessToken = "test-jwt-token";
        String sqlSolution = "SELECT * FROM test_table";

        // Act & Assert - This test verifies the method can be called without throwing exceptions
        assertDoesNotThrow(() -> bajajHealthService.submitSolution(accessToken, sqlSolution));
    }

    @Test
    void testSubmitSolution_WithComplexSql() {
        // Arrange
        String accessToken = "test-jwt-token";
        String complexSql = """
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

        // Act & Assert - This test verifies the method can be called with complex SQL without throwing exceptions
        assertDoesNotThrow(() -> bajajHealthService.submitSolution(accessToken, complexSql));
    }

    @Test
    void testSqlSolutionContent_Question1() {
        // Act
        String result = bajajHealthService.solveQuestion1();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("p.AMOUNT AS SALARY"));
        assertTrue(result.contains("CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME"));
        assertTrue(result.contains("TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE"));
        assertTrue(result.contains("d.DEPARTMENT_NAME"));
        assertTrue(result.contains("FROM PAYMENTS p"));
        assertTrue(result.contains("JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID"));
        assertTrue(result.contains("JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID"));
        assertTrue(result.contains("WHERE DAY(p.PAYMENT_TIME) != 1"));
        assertTrue(result.contains("ORDER BY p.AMOUNT DESC"));
        assertTrue(result.contains("LIMIT 1"));
    }

    @Test
    void testSqlSolutionContent_Question2() {
        // Act
        String result = bajajHealthService.solveQuestion2();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("p.AMOUNT AS SALARY"));
        assertTrue(result.contains("CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME"));
        assertTrue(result.contains("TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE"));
        assertTrue(result.contains("d.DEPARTMENT_NAME"));
        assertTrue(result.contains("FROM PAYMENTS p"));
        assertTrue(result.contains("JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID"));
        assertTrue(result.contains("JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID"));
        assertTrue(result.contains("WHERE DAY(p.PAYMENT_TIME) != 1"));
        assertTrue(result.contains("ORDER BY p.AMOUNT DESC"));
        assertTrue(result.contains("LIMIT 1"));
    }

    @Test
    void testRegistrationNumberParsing() {
        // Test various registration number formats
        String[] testRegNos = {"REG12347", "REG99999", "REG10001", "REG20000"};
        
        for (String regNo : testRegNos) {
            String result = bajajHealthService.solveSqlProblem(regNo);
            assertNotNull(result);
            assertTrue(result.contains("SELECT"));
        }
    }
}
