package com.example.ordermanagement.application.command;

public record PlaceOrderCommand(String product, int quantity, double price) {}