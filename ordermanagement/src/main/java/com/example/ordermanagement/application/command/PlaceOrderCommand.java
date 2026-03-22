package com.example.ordermanagement.application.command;

import com.example.ordermanagement.domain.value.Address;
import java.util.List;

public record PlaceOrderCommand(
        String authUserId,
        Address shippingAddress,
        List<ItemData> items // This refers to the record defined below
) {
    // 1. Adding the missing 'ItemData' symbol here
    public record ItemData(
            String productName,
            int quantity,
            double price
    ) {}
}