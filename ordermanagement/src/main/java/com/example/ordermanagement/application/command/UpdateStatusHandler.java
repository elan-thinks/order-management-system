package com.example.ordermanagement.application.command;

import com.example.ordermanagement.domain.repository.OrderRepository;
import com.example.ordermanagement.domain.model.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Added this

@Service
public class UpdateStatusHandler {
    private final OrderRepository orderRepo;

    public UpdateStatusHandler(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @Transactional // CRITICAL: Ensures the database update "sticks"
    public void handle(UpdateStatusCommand command) {
        java.util.UUID publicId = java.util.UUID.fromString(command.orderId());
        // Search by PublicId instead of the internal database ID
        Order order = orderRepo.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + publicId));
        String status = command.newStatus().toUpperCase();

        // Check which domain method to trigger
        if ("SHIPPED".equals(status)) {
            order.shipOrder();
        }
        else if ("CANCELLED".equals(status)) {
            order.cancelOrder();
        }
        // FIXED: Added the delivery logic here
        else if ("DELIVERED".equals(status)) {
            order.markAsDelivered();
        }

        orderRepo.save(order);
    }
}