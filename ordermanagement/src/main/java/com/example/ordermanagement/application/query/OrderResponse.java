package com.example.ordermanagement.application.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.math.BigDecimal;


public record OrderResponse(
        String orderId,
        String customerName,
        String sku,
        BigDecimal unitPrice,
        int quantity,
        String paymentStatus,
        String orderStatus, // Make sure this matches the HTML
        BigDecimal totalPrice, // Ensure this matches ${order.totalPrice}
        LocalDate orderDate    // Ensure this matches ${order.orderDate}
) {}

//package com.example.ordermanagement.application.query;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import java.time.LocalDate;
//
//@Data // This automatically creates all Getters, Setters, toString, and equals/hashCode
//@AllArgsConstructor
//@NoArgsConstructor
//public class OrderResponse {
//    private String orderId;     // Changed from 'id' to 'orderId' to match your history
//    private String productName;
//    private double unitPrice;
//    private int qty;
//    private String payment;
//    private String orderStatus;
//    private double totalPrice;
//    private LocalDate orderDate;
//
//
//
//}