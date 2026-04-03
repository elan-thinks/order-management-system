package com.example.ordermanagement.application.command;

public record CreateProductCommand(
        String sku,
        String name,
        double price,
        int inventory,
        String category


) {}
