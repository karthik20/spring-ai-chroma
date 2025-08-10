package com.karthik.springaichroma.model;

public record SearchRequest(String query, int limit) {
    public SearchRequest {
        if (limit <= 0) {
            limit = 5;
        }
    }
    
    // Constructor with default limit
    public SearchRequest(String query) {
        this(query, 5);
    }
}