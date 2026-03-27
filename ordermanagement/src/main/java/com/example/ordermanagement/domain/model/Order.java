package com.example.ordermanagement.domain.model;

import com.example.ordermanagement.domain.value.*;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.math.BigDecimal; // <--- ADD THIS LINE
import java.util.*;

public class Order {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID publicId; // External Public ID

//    @Column(name = "customer_id")
    private Long customerId; // Rule 2: Store only the ID reference
//    private Customer customer; // customer customerId
    private Address shippingAddress; //
    private List<OrderItem> items;
    private OrderStatus status;
    private LocalDate createdAt;

    // Constructor for LOADING from Database (This is the one the error is about)
    public Order(Long id, UUID publicId,Long customerId, Address shippingAddress, List<OrderItem> items, OrderStatus status, LocalDate createdAt) {
        this.id = id;
        this.publicId = publicId;
        this.customerId = customerId;
        this.shippingAddress = shippingAddress;
        this.items = new ArrayList<>(items);
        this.status = status;
        this.createdAt = createdAt;
    }

    // Constructor for NEW orders
    // Constructor for NEW orders
    public Order(Long customerId, Address shippingAddress) {
        this.customerId = customerId;
        this.shippingAddress = shippingAddress;
        this.items = new ArrayList<>(); // If this is missing, addItem() crashes!
        this.publicId = UUID.randomUUID();
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDate.now();
    }

    // Getters
    public Long getOrderId() { return id; }
    public UUID getPublicId() { return publicId; }
    public Long getCustomerId() { return customerId; }
    public Address getShippingAddress() { return shippingAddress; }
    public List<OrderItem> getItems() { return items; }
    public OrderStatus getStatus() { return status; }
    public LocalDate getCreatedAt() { return createdAt; }

    // Logic
    public void addItem(OrderItem item) { this.items.add(item); }

    public Money calculateSubtotal() {
        return items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(Money.usd(BigDecimal.ZERO), Money::add);
    }

    public String getPaymentStatus() {

        return (this.status == OrderStatus.PENDING) ? "Unpaid" : "Paid";
    }
    public void markAsDelivered() {
        this.status = OrderStatus.DELIVERED;
    }
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