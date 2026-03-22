package com.example.ordermanagement.domain.value;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record TaxRate(BigDecimal percentage) {
    public Money applyTo(Money amount) {
        BigDecimal taxAmount = amount.amount()
                .multiply(percentage)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        return new Money(taxAmount, amount.currency());
    }
}