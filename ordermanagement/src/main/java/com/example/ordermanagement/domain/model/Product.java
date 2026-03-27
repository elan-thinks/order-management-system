package com.example.ordermanagement.domain.model;

import com.example.ordermanagement.domain.value.Money;

public class Product {
    private Long id;
    private String sku;
    private String name;
    private Money price;
    private String category;
    private int stockQuantity;

    public Product(Long id, String sku, String name, Money price, int stockQuantity, String category) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.category = category;
        this.stockQuantity = stockQuantity;
    }

    public void reduceStock(int quantity) {
        if (this.stockQuantity < quantity) {
            throw new IllegalStateException("Not enough stock");
        }
        this.stockQuantity -= quantity;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Money getPrice() { return price; }
    public void setPrice(Money price) { this.price = price; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}