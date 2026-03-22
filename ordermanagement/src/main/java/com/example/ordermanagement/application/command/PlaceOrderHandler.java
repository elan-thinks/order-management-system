package com.example.ordermanagement.application.command;

import com.example.ordermanagement.domain.factory.OrderFactory;
import com.example.ordermanagement.domain.model.*;
import com.example.ordermanagement.domain.repository.*;
import com.example.ordermanagement.domain.value.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaceOrderHandler {
    private final OrderRepository orderRepo;
    private final CustomerRepository customerRepo;
    private final OrderFactory orderFactory;

    public PlaceOrderHandler(OrderRepository orderRepo,
                             CustomerRepository customerRepo,
                             OrderFactory orderFactory) {
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
        this.orderFactory = orderFactory;
    }

    @Transactional
    public void handle(PlaceOrderCommand command) {
        // 1. Find the customer
        Customer customer = customerRepo.findByAuthId(command.authUserId())
                .orElseThrow(() -> new RuntimeException("Customer not registered"));

        // 2. CONVERT: Map ItemData (Command) to OrderItem (Domain)
        List<OrderItem> domainItems = command.items().stream()
                .map(item -> new OrderItem(
                        item.productName(), // SKU
                        Money.usd(BigDecimal.valueOf(item.price())), // Price as Money
                        item.quantity() // Qty
                ))
                .collect(Collectors.toList());

        // 3. Create the aggregate using the factory with the correct list type
        Order order = orderFactory.createOrder(customer, command.shippingAddress(), domainItems);

        // 4. Persist
        orderRepo.save(order);
    }
}