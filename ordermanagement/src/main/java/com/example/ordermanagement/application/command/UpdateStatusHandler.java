package com.example.ordermanagement.application.command;

import com.example.ordermanagement.domain.repository.OrderRepository;
import com.example.ordermanagement.domain.model.Order;
import org.springframework.stereotype.Service;

@Service
public class UpdateStatusHandler {
    private final OrderRepository orderRepo;

    public UpdateStatusHandler(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    public void handle(UpdateStatusCommand command) {
        Order order = orderRepo.findById(command.orderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // The "Rich" domain handles the state transition logic
        if ("SHIPPED".equalsIgnoreCase(command.newStatus())) {
            order.shipOrder();
        } else if ("CANCELLED".equalsIgnoreCase(command.newStatus())) {
            order.cancelOrder();
        }

        orderRepo.save(order);
    }
}