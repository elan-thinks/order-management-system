package com.example.ordermanagement.application.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // <-- This is what creates the 7-argument constructor
@NoArgsConstructor  // <-- This creates the 0-argument constructor
public class OrderResponse {
    private String orderId;
    private String productName;
    private int qty;
    private double unitPrice;
    private double total;
    private String payment;
    private String orderStatus;
}