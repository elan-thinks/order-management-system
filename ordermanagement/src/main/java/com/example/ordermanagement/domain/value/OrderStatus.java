package com.example.ordermanagement.domain.value;

public enum OrderStatus {
    PENDING,    // Created but not paid
    PAID,       // Payment confirmed
    SHIPPED,    // Out for delivery
    DELIVERED,  // Received by customer
    CANCELLED   // Terminated
}
