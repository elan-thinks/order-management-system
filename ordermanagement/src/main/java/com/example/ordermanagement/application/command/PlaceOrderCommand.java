package com.example.ordermanagement.application.command;

import java.util.List;

public record PlaceOrderCommand(
        List<ItemData> items
) {
    public record ItemData(String product, int quantity, double price) {}
}