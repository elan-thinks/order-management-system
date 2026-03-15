package com.example.ordermanagement.application.query;

import com.example.ordermanagement.domain.value.OrderItem;
import com.example.ordermanagement.domain.value.Money;

public record OrderItemResponse(
        String product,
        int quantity,
        double price // using double for DTO/API
) {
    public static OrderItemResponse fromDomain(OrderItem item) {
        return new OrderItemResponse(
                item.getProduct(),
                item.getQuantity(),
                item.getPrice() // convert Money -> double
        );
    }
}