package com.example.ordermanagement.domain.model;

import com.example.ordermanagement.domain.value.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "order_id") // This must match the DB column name exactly
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private List<OrderItem> items = new ArrayList<>();

    private LocalDate createdAt;

    @Embedded
    private Address shippingAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // JPA Requirement
    protected Order() {}

    // Constructor for NEW orders (Used by OrderFactory)
    public Order(String orderId, Customer customer, Address shippingAddress) {
        this.orderId = orderId;
        this.customer = customer;
        this.shippingAddress = shippingAddress;
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDate.now();
    }

    // Constructor for LOADING orders from DB (Used by OrderEntity)
    public Order(String orderId, Customer customer, Address shippingAddress, List<OrderItem> items, OrderStatus status, LocalDate createdAt) {
        this.orderId = orderId;
        this.customer = customer;
        this.shippingAddress = shippingAddress;
        this.items = new ArrayList<>(items);
        this.status = status;
        this.createdAt = createdAt;
    }

    // --- DOMAIN LOGIC ---

    public void addItem(OrderItem item) {
        this.items.add(item);
    }

    public Money calculateSubtotal() {
        return items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(Money.usd(BigDecimal.ZERO), Money::add);
    }

    public void shipOrder() {
        if (this.status != OrderStatus.PAID && this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("Order must be PENDING or PAID to be shipped.");
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
            throw new IllegalStateException("Cannot cancel an order that has already been " + this.status);
        }
        this.status = OrderStatus.CANCELLED;
    }

    // --- GETTERS (Crucial for OrderEntity mapping) ---

    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public OrderStatus getStatus() {
        return status;
    }

    // Helper for UI/Dashboard
    public String getPaymentStatus() {
        return (this.status == OrderStatus.PENDING) ? "Unpaid" : "Paid";
    }
}