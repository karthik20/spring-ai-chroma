package com.example.springaichroma.model;

public class SearchRequest {
    private String query;
    private int limit = 5;

    public SearchRequest() {}

    public SearchRequest(String query, int limit) {
        this.query = query;
        this.limit = limit;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}