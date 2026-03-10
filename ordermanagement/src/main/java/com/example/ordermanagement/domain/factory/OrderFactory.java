package com.example.ordermanagement.domain.factory;

import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.repository.OrderRepository;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class OrderFactory {

    private final OrderRepository repository;

    public OrderFactory(OrderRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a professional Order with ORDXXXX formatting.
     */
    public Order createOrder(String product, int quantity, double price) {
        String uniqueId = "ORD-" + System.currentTimeMillis();
        // 1. Business Rule: Validation
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than zero");

        // 2. ID Generation: Get current count from DB to create sequential ID
        long nextId = repository.count() + 1;
        String formattedId = String.format("ORD%04d", nextId);

        // 3. Defaults: New orders start as 'Pending' and 'Unpaid'
        return new Order(
                uniqueId,
                product,
                quantity,
                price,
                "Pending", // Default status for UI
                "Unpaid",  // Default payment for UI
                LocalDate.now()
        );
    }
}