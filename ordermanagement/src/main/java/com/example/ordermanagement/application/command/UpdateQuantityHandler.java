package com.example.ordermanagement.application.command;

import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.model.OrderItem;
import com.example.ordermanagement.domain.value.Money;
import com.example.ordermanagement.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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

        OrderItem targetItem = null;
        for (OrderItem item : order.getItems()) {
            if (item.getProduct().equals(command.productName())) {
                targetItem = item;
                break;
            }
        }

        if (targetItem == null) throw new RuntimeException("Product not found in order");

        // 1. Remove the old item
        order.getItems().remove(targetItem);

        // 2. Create the new Money object using the static 'usd' helper
        // This fixes the "actual and formal argument lists differ in length" error
        BigDecimal priceAsBigDecimal = BigDecimal.valueOf(targetItem.getPrice());
        Money unitPrice = Money.usd(priceAsBigDecimal);

        // 3. Add the updated item (Correct order: SKU, Money, Quantity)
        order.addItem(new OrderItem(targetItem.getSku(), unitPrice, command.newQuantity()));

        repository.save(order);
    }
}