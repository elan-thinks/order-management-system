package com.example.ordermanagement.application.command;

import com.example.ordermanagement.domain.value.Address;
import java.util.List;

public record PlaceOrderCommand(
        String authUserId,   // This will be "Ema"
        String productName,  // Matches name="productName" in HTML
        int qty,             // Matches name="qty" in HTML
        Address shippingAddress
) {
    // 1. Adding the missing 'ItemData' symbol here
    public record ItemData(
            String productName,
            int quantity,
            double price
    ) {}
}