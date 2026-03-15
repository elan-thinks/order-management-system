package com.example.ordermanagement.application.command;

import com.example.ordermanagement.domain.factory.OrderFactory;
import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.value.Money;
import com.example.ordermanagement.domain.value.OrderItem;
import com.example.ordermanagement.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PlaceOrderHandler {
    private final OrderFactory factory;
    private final OrderRepository repository;

    public PlaceOrderHandler(OrderFactory factory, OrderRepository repository) {
        this.factory = factory;
        this.repository = repository;
    }

    @Transactional
    public void handle(PlaceOrderCommand command) {
        List<OrderItem> orderItems = command.items().stream()
                .map(i -> new OrderItem(i.product(), i.quantity(), new Money(i.price())))
                .toList();

        Order order = factory.createOrder(orderItems);

        // Calculate total quantity from items and set it on the parent Order
        int totalQty = command.items().stream().mapToInt(i -> i.quantity()).sum();
        order.setQuantity(totalQty); // This satisfies the DB constraint!

        repository.save(order);
    }
}