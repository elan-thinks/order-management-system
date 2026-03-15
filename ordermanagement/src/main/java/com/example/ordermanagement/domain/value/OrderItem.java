package com.example.ordermanagement.domain.value;

public class OrderItem {
    private final String product;
    private final int quantity;
    private final Money price;

    public OrderItem(String product, int quantity, Money price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price.getAmount(); }
    public double getTotal() { return price.getAmount() * quantity; }
}