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
    private  String orderId;

    // Inside Order.java
    // Inside your Order class
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "authId") // Changed from auth_user_id to authId
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id") // Creates a foreign key in order_items table
    private List<OrderItem> items = new ArrayList<>();


    private  LocalDate createdAt;

    @Embedded // If Address is a record/class with @Embeddable
    private Address shippingAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // JPA requires a protected/public no-args constructor
    protected Order() {}

    // 1. Constructor for NEW Orders (Used by OrderFactory)
    // Constructor for NEW orders
    public Order(String orderId, Customer customer, Address shippingAddress) {
        this.orderId = orderId;
        this.customer = Objects.requireNonNull(customer);
        this.shippingAddress = shippingAddress;
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDate.now();
    }

    // Constructor for EXISTING orders (Reconstruction)
    public Order(String orderId, Customer customer, List<OrderItem> items, OrderStatus status, LocalDate createdAt) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = new ArrayList<>(items);
        this.status = status;
        this.createdAt = createdAt;
    }

    public void addItem(OrderItem item) {
        // Rule 1: Don't allow changes if the order is already SHIPPED or CANCELLED
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("Cannot add items to an order in " + this.status + " status.");
        }

        // Rule 2: Validation (e.g., preventing zero-quantity items)
        // Cast the result to Integer so Java can treat it as a number
        Integer qty = (Integer) item.getQuantity();

        if (qty == null || qty <= 0) {
            throw new IllegalArgumentException("Item quantity must be greater than zero.");
        }

        // Rule 3: Avoid duplicates (Optional logic)
        // If an item with the same SKU exists, you could choose to increase its quantity instead
        this.items.add(item);
    }

    public Money calculateSubtotal() {
        return items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(Money.usd(BigDecimal.ZERO), Money::add);
    }

    public void addProduct(Product product, int qty) {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("Cannot modify a finalized order");
        }
        product.reduceStock(qty);
        this.items.add(new OrderItem(product.getSku(), product.getPrice(), qty));
    }

    public void markAsPaid() {
        if (this.status != OrderStatus.PENDING) throw new IllegalStateException("Invalid state transition");
        this.status = OrderStatus.PAID;
    }

    public void shipOrder() {
        if (this.status != OrderStatus.PAID) throw new IllegalStateException("Must be PAID to ship");
        this.status = OrderStatus.SHIPPED;
    }

    // Getters for UI/Persistence
    public String getOrderId() { return orderId; }
    public OrderStatus getStatus() { return status; }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    // In Order.java
    public String getPaymentStatus() { // Changed from Object
        // Logic to match your UI's "Paid"/"Unpaid" requirement
        return (this.status == OrderStatus.PAID ||
                this.status == OrderStatus.SHIPPED ||
                this.status == OrderStatus.DELIVERED) ? "Paid" : "Unpaid";
    }

    public java.time.LocalDate getCreatedAt() { // Changed from Object
        return java.time.LocalDate.now(); // Or your date field
    }


//    public double getTotalPrice() {
//    }

    public Object getId() {
        return orderId;
    }

    // 1. Fix the missing cancelOrder method
    public void cancelOrder() {
        // Business Rule: Cannot cancel if already shipped or delivered
        if (this.status == OrderStatus.SHIPPED || this.status == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel an order that has already been " + this.status);
        }
        this.status = OrderStatus.CANCELLED;
    }

    // 2. Add a method for Delivery (to match your "DONE" button in the UI)
    public void deliverOrder() {
        if (this.status != OrderStatus.SHIPPED) {
            throw new IllegalStateException("Order must be SHIPPED before it can be DELIVERED");
        }
        this.status = OrderStatus.DELIVERED;
    }

    // 3. Fix the Total Price for the Seller Dashboard
    public double getTotalPrice() {
        return calculateSubtotal().amount().doubleValue();
    }

    // This bridges 'createdAt' in Java to 'orderDate' in your HTML
    public java.time.LocalDate getOrderDate() {
        return this.createdAt;
    }

    // This bridges the 'status' Enum to 'orderStatus' in your HTML
    public String getOrderStatus() {
        return this.status.name();
    }

//    public Object getPayment() {
//
//    }
//
//    public Object getDate() {
//    }
}