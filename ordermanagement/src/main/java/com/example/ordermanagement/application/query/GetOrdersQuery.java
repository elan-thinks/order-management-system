package com.example.ordermanagement.application.query;

public record GetOrdersQuery(String searchTerm) {

    // This allows you to call new GetOrdersQuery() with no arguments too
    public GetOrdersQuery() {
        this(null);
    }
}