package com.example.ordermanagement.application.command;

public record UpdateQuantityCommand(String orderId, int newQuantity) {}
