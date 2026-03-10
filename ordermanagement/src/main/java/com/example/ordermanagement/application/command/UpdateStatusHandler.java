package com.example.ordermanagement.application.command;

import com.example.ordermanagement.infrastructure.persistence.OrderEntity;
import com.example.ordermanagement.infrastructure.persistence.SpringDataOrderRepository;
import org.springframework.transaction.annotation.Transactional; // Add this import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateStatusHandler {

    @Autowired
    private SpringDataOrderRepository repository;

    @Transactional
    public void handle(String orderId, String newStatus) {
        repository.findById(orderId).ifPresent(order -> {
            // Using setStatus because that is the name in your OrderEntity
            order.setStatus(newStatus);
            order.setPayment("Paid");   // Automatically mark as Paid
            repository.save(order);
        });
    }

}