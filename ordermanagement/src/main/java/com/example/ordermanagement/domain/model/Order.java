package com.example.ordermanagement.domain.model;

import com.example.ordermanagement.domain.value.*;
import java.time.LocalDate;
import java.math.BigDecimal; // <--- ADD THIS LINE
import java.util.*;

public class Order {
    private String id;
    private Customer customer;
    private Address shippingAddress; // The field must exist
    private List<OrderItem> items;
    private OrderStatus status;
    private LocalDate createdAt;

    // Constructor for LOADING from Database (This is the one the error is about)
    public Order(String id, Customer customer, Address shippingAddress, List<OrderItem> items, OrderStatus status, LocalDate createdAt) {
        this.id = id;
        this.customer = customer;
        this.shippingAddress = shippingAddress;
        this.items = new ArrayList<>(items);
        this.status = status;
        this.createdAt = createdAt;
    }

    // Constructor for NEW orders
    public Order(String id, Customer customer, Address shippingAddress) {
        this(id, customer, shippingAddress, new ArrayList<>(), OrderStatus.PENDING, LocalDate.now());
    }
    public Money calculateSubtotal() {
        return items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(Money.usd(BigDecimal.ZERO), Money::add);
    }
    // --- ADD THIS METHOD TO FIX THE ERROR ---
    public String getPaymentStatus() {
        // If the order is PENDING, it's "Unpaid".
        // If it's SHIPPED, DELIVERED, or PAID, it's "Paid".
        return (this.status == OrderStatus.PENDING) ? "Unpaid" : "Paid";
    }
    public void markAsDelivered() {
        this.status = OrderStatus.DELIVERED;
    }

    // Getters
    public String getOrderId() { return id; }
    public Customer getCustomer() { return customer; }
    public Address getShippingAddress() { return shippingAddress; }
    public List<OrderItem> getItems() { return items; }
    public OrderStatus getStatus() { return status; }
    public LocalDate getCreatedAt() { return createdAt; }

    // Logic
    public void addItem(OrderItem item) { this.items.add(item); }

    public void shipOrder() {
        if (this.status != OrderStatus.PENDING && this.status != OrderStatus.PAID) {
            throw new IllegalStateException("Only PENDING or PAID orders can be shipped.");
        }
        this.status = OrderStatus.SHIPPED;
    }

    public void deliverOrder() {
        if (this.status != OrderStatus.SHIPPED) {
            throw new IllegalStateException("Order must be SHIPPED before it can be DELIVERED.");
        }
        this.status = OrderStatus.DELIVERED;
    }

    public void cancelOrder() {
        if (this.status == OrderStatus.SHIPPED || this.status == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel an order that is already shipped or delivered.");
        }
        this.status = OrderStatus.CANCELLED;
    }
}