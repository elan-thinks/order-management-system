package com.example.ordermanagement.domain.value;

public record ProductDetails(String name, String description, String category) {
    public ProductDetails {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Product name is required");
    }
}