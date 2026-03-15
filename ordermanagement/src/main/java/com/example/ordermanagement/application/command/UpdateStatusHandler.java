package com.example.ordermanagement.application.command;

import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UpdateStatusHandler {
    private final OrderRepository repository;

    public UpdateStatusHandler(OrderRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void handle(UpdateStatusCommand command) {
        // 1. Get the Optional from the repository
        Optional<Order> orderOptional = repository.findById(command.orderId());

        // 2. Open the "box" and perform actions if the order exists
        orderOptional.ifPresent(order -> {
            order.deliver(); // Now calling deliver() on the actual Order object
            repository.save(order); // Saving the actual Order object
        });
    }
}