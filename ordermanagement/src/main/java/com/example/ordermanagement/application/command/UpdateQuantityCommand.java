package com.example.ordermanagement.application.command;

// Command
public record UpdateQuantityCommand(
        String orderId,
        String productName,
        int newQuantity
) {}
