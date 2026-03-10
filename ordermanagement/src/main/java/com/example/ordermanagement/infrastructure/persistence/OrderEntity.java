package com.example.ordermanagement.infrastructure.persistence;

import com.example.ordermanagement.domain.model.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    private String id;
    private String product;
    private int quantity;
    private double price;
    private String status;
    private String payment;
    private LocalDate orderDate;

    // This is the missing method causing your error!
    public static OrderEntity fromDomain(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.id = order.getId();
        entity.product = order.getProduct();
        entity.quantity = order.getQuantity();
        entity.price = order.getPrice();
        entity.status = order.getStatus();
        entity.payment = order.getPayment();
        entity.orderDate = order.getDate();
        return entity;
    }

    // You also need this to convert database results back to the Domain
    public Order toDomain() {
        return new Order(
                this.id,
                this.product,
                this.quantity,
                this.price,
                this.status,
                this.payment,
                this.orderDate
        );
    }

    // Standard Getters and Setters (Required for JPA)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPayment() { return payment; }
    public void setPayment(String payment) { this.payment = payment; }
    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
}