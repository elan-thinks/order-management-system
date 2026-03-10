package com.example.ordermanagement.application.command;

import com.example.ordermanagement.domain.factory.OrderFactory;
import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.repository.OrderRepository;
import org.springframework.transaction.annotation.Transactional;
//import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PlaceOrderHandler {
    private final OrderRepository repository;
    private final OrderFactory orderFactory; // Ensure the factory is injected

    public PlaceOrderHandler(OrderRepository repository, OrderFactory orderFactory) {
        this.repository = repository;
        this.orderFactory = orderFactory;
    }

    @Transactional // <--- ADD THIS LINE HERE
    public void handle(PlaceOrderCommand command) {
        Order order = orderFactory.createOrder(
                command.product(),
                command.quantity(),
                command.price()
        );
        repository.save(order);
        System.out.println("DEBUG: Order saved to database: " + order.getId());
    }
    @Service
    public class UpdateQuantityHandler {
        private final OrderRepository repository;

        public UpdateQuantityHandler(OrderRepository repository) {
            this.repository = repository;
        }
    }
}