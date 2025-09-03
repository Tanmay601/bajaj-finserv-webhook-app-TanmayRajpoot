package com.bajaj.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SolutionRequestTest {

    @Test
    void testDefaultConstructor() {
        SolutionRequest request = new SolutionRequest();
        assertNotNull(request);
        assertNull(request.getFinalQuery());
    }

    @Test
    void testParameterizedConstructor() {
        String sqlQuery = "SELECT * FROM employees WHERE salary > 50000";
        SolutionRequest request = new SolutionRequest(sqlQuery);

        assertEquals(sqlQuery, request.getFinalQuery());
    }

    @Test
    void testSettersAndGetters() {
        SolutionRequest request = new SolutionRequest();

        String sqlQuery = "SELECT name, salary FROM employees ORDER BY salary DESC";
        request.setFinalQuery(sqlQuery);

        assertEquals(sqlQuery, request.getFinalQuery());
    }

    @Test
    void testToString() {
        String sqlQuery = "SELECT * FROM test_table";
        SolutionRequest request = new SolutionRequest(sqlQuery);
        String result = request.toString();

        assertTrue(result.contains(sqlQuery));
        assertTrue(result.contains("SolutionRequest"));
    }

    @Test
    void testEqualsAndHashCode() {
        SolutionRequest request1 = new SolutionRequest("SELECT * FROM table1");
        SolutionRequest request2 = new SolutionRequest("SELECT * FROM table1");
        SolutionRequest request3 = new SolutionRequest("SELECT * FROM table2");

        // Test equals
        assertEquals(request1.getFinalQuery(), request2.getFinalQuery());
        
        // Test different objects
        assertNotEquals(request1.getFinalQuery(), request3.getFinalQuery());
    }

    @Test
    void testComplexSqlQuery() {
        String complexQuery = """
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

        SolutionRequest request = new SolutionRequest(complexQuery);
        assertEquals(complexQuery, request.getFinalQuery());
    }
}
