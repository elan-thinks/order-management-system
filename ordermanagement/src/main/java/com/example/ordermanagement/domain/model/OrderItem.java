package com.example.ordermanagement.domain.model;

import com.example.ordermanagement.domain.value.Money;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;

    @Embedded
    private Money unitPrice;

    private int quantity;

    protected OrderItem() {} // Required for JPA

    public OrderItem(String sku, Money unitPrice, int quantity) {
        this.sku = sku;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    // BUSINESS LOGIC: The item calculates its own cost
    public Money getSubtotal() {
        return unitPrice.multiply(quantity);
    }

    // In OrderItem.java
    public String getSku() { // Changed from Object
        return sku;
    }

    public int getQuantity() { // Changed from Object
        return quantity;
    }

    // Fixed: Returning your domain Money object instead of ExpiresFilter
    public Money getUnitPrice() {
        return unitPrice;
    }


    // Added to support your GetOrdersHandler mapping
    public double getPrice() {
        return unitPrice.amount().doubleValue();
    }

    // For debugging/display purposes
    public String getProduct() {
        return sku;
    }
}