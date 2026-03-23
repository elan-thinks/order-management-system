package com.example.ordermanagement.domain.model;

import com.example.ordermanagement.domain.value.Money;
import jakarta.persistence.*; // You must add this import
import java.util.UUID;

@Entity // 1. Tells Hibernate this is a table
@Table(name = "products")
public class Product {

    @Id // 2. Marks the primary key
    private UUID id;

    @Column(unique = true, nullable = false)
    private String sku;

    private String name;

    @Embedded // 3. Maps the Money record columns to this table
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "price_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
    })
    private Money price;

    private int stockQuantity;
    private String category; // Added to match your controller
    private String status;   // Added to match your controller

    // 4. JPA REQUIRED: A no-args constructor
    protected Product() {}

    public Product(String sku, String name, Money price, int stockQuantity) {
        this.id = UUID.randomUUID();
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // --- Business Logic ---
    public boolean hasStock(int quantity) {
        return this.stockQuantity >= quantity;
    }
// inside Product.java

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    // Optional: A more "Domain-Driven" way to do it
    public void reduceStock(int quantity) {
        if (this.stockQuantity < quantity) {
            throw new IllegalStateException("Not enough stock for product: " + this.name);
        }
        this.stockQuantity -= quantity;
    }

    // --- Getters & Setters ---
    public UUID getId() { return id; }
    public String getSku() { return sku; }
    public String getName() { return name; }
    public Money getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }

    // Make sure these actually assign the values!
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setInventory(int inventory) { this.stockQuantity = inventory; }
    public void setStatus(String status) { this.status = status; }
}