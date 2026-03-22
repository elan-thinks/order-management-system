package com.example.ordermanagement.application.command;

public record CreateProductCommand(
        String name,
        String sku,
        String category,
        double price,
        int inventory
) {}
