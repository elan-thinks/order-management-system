package com.example.ordermanagement.application.command;

import com.example.ordermanagement.domain.repository.OrderRepository;
import com.example.ordermanagement.domain.model.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateQuantityHandler {
    private final OrderRepository repository;

    public UpdateQuantityHandler(OrderRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void handle(UpdateQuantityCommand command) {
        // Use String ID to find the order
        Order order = repository.findById(command.orderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Create the updated domain object with all 7 fields
        Order updatedOrder = new Order(
                order.getId(),
                order.getProduct(),
                command.newQuantity(), // The change
                order.getPrice(),
                order.getStatus(),
                order.getPayment(),
                order.getDate()
        );

        repository.save(updatedOrder);
    }
}