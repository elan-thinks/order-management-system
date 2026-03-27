package com.example.ordermanagement.application.command;

import com.example.ordermanagement.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteOrderHandler {
    private final OrderRepository repository;

    public DeleteOrderHandler(OrderRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void handle(DeleteOrderCommand command) {
        // FIXED: Convert the String ID from the command to a Long
        Long id = Long.valueOf(command.orderId());

        repository.deleteById(id);
    }
}