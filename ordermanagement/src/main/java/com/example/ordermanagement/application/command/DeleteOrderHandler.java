package com.example.ordermanagement.application.command;

import com.example.ordermanagement.infrastructure.persistence.SpringDataOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteOrderHandler {
    @Autowired
    private SpringDataOrderRepository repository;

    public void handle(String orderId) {
        repository.deleteById(orderId);
    }
}
