package com.bajaj.dto;

public class SolutionRequest {
    private String finalQuery;

    // Default constructor
    public SolutionRequest() {}

    // Constructor with parameters
    public SolutionRequest(String finalQuery) {
        this.finalQuery = finalQuery;
    }

    // Getters and Setters
    public String getFinalQuery() {
        return finalQuery;
    }

    public void setFinalQuery(String finalQuery) {
        this.finalQuery = finalQuery;
    }

    @Override
    public String toString() {
        return "SolutionRequest{" +
                "finalQuery='" + finalQuery + '\'' +
                '}';
    }
}
