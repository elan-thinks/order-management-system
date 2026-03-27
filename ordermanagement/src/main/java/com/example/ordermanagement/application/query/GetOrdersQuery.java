package com.example.ordermanagement.application.query;

// Add the 'searchTerm' field so the constructor can accept a String
public record GetOrdersQuery(String searchTerm) {
    // This allows you to call new GetOrdersQuery() with no arguments too
    public GetOrdersQuery() {
        this(null);
    }
}