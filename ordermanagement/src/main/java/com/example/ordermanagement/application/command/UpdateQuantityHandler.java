package com.example.ordermanagement.application.command;

import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.value.OrderItem;
import com.example.ordermanagement.domain.value.Money;
import com.example.ordermanagement.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Handler
@Service
public class UpdateQuantityHandler {

    private final OrderRepository repository;

    public UpdateQuantityHandler(OrderRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void handle(UpdateQuantityCommand command) {

        Order order = repository.findById(command.orderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        boolean updated = false;
        for (OrderItem item : order.getItems()) {
            if (item.getProduct().equals(command.productName())) {
                order.getItems().remove(item);
                order.addItem(new OrderItem(item.getProduct(), command.newQuantity(), new Money(item.getPrice())));
                updated = true;
                break;
            }
        }

        if (!updated) throw new RuntimeException("Product not found in order");

        repository.save(order);
    }
}