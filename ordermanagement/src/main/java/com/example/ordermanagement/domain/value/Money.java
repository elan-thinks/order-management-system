package com.example.ordermanagement.domain.value;



import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Currency;
@Embeddable // Allows this record to be part of an @Entity
public record Money(BigDecimal amount, Currency currency) {
    public static Money usd(BigDecimal amount) {
        return new Money(amount, Currency.getInstance("USD"));
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) throw new IllegalArgumentException("Currency mismatch");
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money multiply(int factor) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(factor)), this.currency);
    }
}