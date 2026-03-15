package com.example.ordermanagement.domain.model;

import com.example.ordermanagement.domain.value.OrderItem;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private final String id;
    private final LocalDate date;
    private String status;
    private String payment;
    private int quantity;
    private final List<OrderItem> items = new ArrayList<>();

    public Order(String id, String status, String payment, LocalDate date) {
        this.id = id;
        this.status = status;
        this.payment = payment;
        this.date = date;
    }

    // THIS WAS MISSING - Add it back!
    public void addItem(OrderItem item) {
        this.items.add(item);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public void deliver() {
        this.status = "Delivered";
        this.payment = "Paid";
    }

    public double getTotalPrice() {
        return items.stream().mapToDouble(OrderItem::getTotal).sum();
    }

    // Getters
    public String getId() { return id; }
    public String getStatus() { return status; }
    public String getPayment() { return payment; }
    public int getQuantity() { return quantity; }
    public LocalDate getDate() { return date; }
    public List<OrderItem> getItems() { return items; }
}