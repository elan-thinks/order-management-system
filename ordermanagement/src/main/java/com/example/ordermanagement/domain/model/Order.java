package com.example.ordermanagement.domain.model;

import java.time.LocalDate;

/**
 * Updated Domain Model to match the Prodlytics UI requirements.
 * It now includes status and payment tracking.
 */
public class Order {
    private final String id;         // Changed from UUID to String for "ORD0001" format
    private final String product;
    private final int quantity;
    private final double price;
    private final String status;      // For: Delivered, Pending, Shipped
    private final String payment;     // For: Paid, Unpaid
    private final LocalDate date;     // For the "Date" column in your UI

    // Constructor updated with the new fields
    public Order(String id, String product, int quantity, double price, String status, String payment, LocalDate date) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.payment = payment;
        this.date = date;
    }

    // Getters for all fields
    public String getId() { return id; }
    public String getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public String getStatus() { return status; }
    public String getPayment() { return payment; }
    public LocalDate getDate() { return date; }
}