package com.example.ordermanagement.domain.value;

public class Money {
    private final double amount;

    public Money(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public Money multiply(int qty) {
        return new Money(this.amount * qty);
    }
}