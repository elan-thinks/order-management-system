package com.example.ordermanagement.application.command;

public record UpdateStatusCommand(String orderId, String newStatus) {}