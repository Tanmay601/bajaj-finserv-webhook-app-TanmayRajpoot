package com.bajaj.service;

import com.bajaj.dto.SolutionRequest;
import com.bajaj.dto.WebhookRequest;
import com.bajaj.dto.WebhookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class BajajHealthService {

    private static final Logger logger = LoggerFactory.getLogger(BajajHealthService.class);
    
    private static final String WEBHOOK_GENERATION_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
    private static final String SOLUTION_SUBMISSION_URL = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";
    
    // Using both RestTemplate and WebClient as per requirements
    private final RestTemplate restTemplate;
    private final WebClient webClient;

    @Autowired
    public BajajHealthService(RestTemplate restTemplate, WebClient webClient) {
        this.restTemplate = restTemplate;
        this.webClient = webClient;
    }

    /**
     * This method is triggered on application startup as per requirements
     * The flow is not triggered by a controller or endpoint
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        logger.info("Application started. Beginning Bajaj Health service flow...");
        
        try {
            // Step 1: Generate webhook
            WebhookResponse webhookResponse = generateWebhook();
            logger.info("Webhook generated successfully: {}", webhookResponse.getWebhook());
            
            // Step 2: Solve SQL problem based on registration number
            String sqlSolution = solveSqlProblem("REG12347"); // Using the registration number from the request
            logger.info("SQL problem solved. Solution: {}", sqlSolution);
            
            // Step 3: Submit solution using JWT token
            submitSolution(webhookResponse.getAccessToken(), sqlSolution);
            logger.info("Solution submitted successfully!");
            
        } catch (Exception e) {
            logger.error("Error in Bajaj Health service flow: ", e);
        }
    }

    /**
     * Step 1: Generate webhook using RestTemplate
     */
    public WebhookResponse generateWebhook() {
        logger.info("Generating webhook...");
        
        WebhookRequest request = new WebhookRequest("John Doe", "REG12347", "john@example.com");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<WebhookRequest> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<WebhookResponse> response = restTemplate.postForEntity(
            WEBHOOK_GENERATION_URL, 
            entity, 
            WebhookResponse.class
        );
        
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to generate webhook. Status: " + response.getStatusCode());
        }
    }

    /**
     * Step 2: Solve SQL problem based on registration number
     * Determines which question to solve based on last two digits
     */
    public String solveSqlProblem(String regNo) {
        logger.info("Solving SQL problem for registration number: {}", regNo);
        
        // Extract last two digits from registration number
        String lastTwoDigits = regNo.substring(regNo.length() - 2);
        int lastTwoDigitsInt = Integer.parseInt(lastTwoDigits);
        
        if (lastTwoDigitsInt % 2 == 0) {
            // Even - Question 2
            logger.info("Last two digits are even ({}). Solving Question 2", lastTwoDigits);
            return solveQuestion2();
        } else {
            // Odd - Question 1
            logger.info("Last two digits are odd ({}). Solving Question 1", lastTwoDigits);
            return solveQuestion1();
        }
    }

    /**
     * Solve Question 1 (for odd registration numbers)
     * Based on the database schema with DEPARTMENT, EMPLOYEE, and PAYMENTS tables
     */
    public String solveQuestion1() {
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

    /**
     * Solve Question 2 (for even registration numbers)
     * Based on the RV_Q document - using the same schema as Question 1
     */
    public String solveQuestion2() {
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

    /**
     * Step 3: Submit solution using WebClient with JWT token
     */
    public void submitSolution(String accessToken, String sqlSolution) {
        logger.info("Submitting solution with JWT token...");
        
        SolutionRequest request = new SolutionRequest(sqlSolution);
        
        webClient.post()
            .uri(SOLUTION_SUBMISSION_URL)
            .header("Authorization", accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), SolutionRequest.class)
            .retrieve()
            .bodyToMono(String.class)
            .doOnSuccess(response -> logger.info("Solution submitted successfully: {}", response))
            .doOnError(error -> logger.error("Error submitting solution: ", error))
            .subscribe();
    }
}
