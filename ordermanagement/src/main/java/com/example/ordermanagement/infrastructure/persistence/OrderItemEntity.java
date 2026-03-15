package com.example.ordermanagement.infrastructure.persistence;

import com.example.ordermanagement.domain.value.Money;
import com.example.ordermanagement.domain.value.OrderItem;
import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String product;
    private int quantity;
    private double price;

    public OrderItemEntity() {}

    public static OrderItemEntity fromDomain(OrderItem item) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.product = item.getProduct();
        entity.quantity = item.getQuantity();
        entity.price = item.getPrice();
        return entity;
    }

    public OrderItem toDomain() {
        return new OrderItem(product, quantity, new Money(price));
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}