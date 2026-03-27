package com.example.ordermanagement.domain.model;

import com.example.ordermanagement.domain.value.Money;

public class OrderItem {
    private String sku;
    private Money unitPrice;
    private int quantity;

    public OrderItem(String sku, Money unitPrice, int quantity) {
        this.sku = sku;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public double getPrice() {
        return unitPrice.amount().doubleValue();
    }

    public String getSku() {
        return sku;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProduct() {
        return sku;
    }

    public Money getSubtotal() {
        return unitPrice.multiply(quantity);
    }
}