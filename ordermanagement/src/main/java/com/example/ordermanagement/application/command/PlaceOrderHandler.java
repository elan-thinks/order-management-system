package com.example.ordermanagement.application.command;

import com.example.ordermanagement.domain.factory.OrderFactory;
import com.example.ordermanagement.domain.model.*;
import com.example.ordermanagement.domain.repository.*; // This imports ProductRepository
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
    private final ProductRepository productRepository; // FIXED: Added this field
    private final OrderFactory orderFactory;

    // FIXED: Added productRepository to the constructor for Dependency Injection
    public PlaceOrderHandler(OrderRepository orderRepo,
                             CustomerRepository customerRepo,
                             ProductRepository productRepository,
                             OrderFactory orderFactory) {
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
        this.productRepository = productRepository;
        this.orderFactory = orderFactory;
    }

    @Transactional
    public void handle(PlaceOrderCommand command) {
        // 1. Find the customer
        Customer customer = customerRepo.findByAuthId(command.authUserId())
                .orElseThrow(() -> new RuntimeException("Customer not registered"));

        // 2. CONVERT & VALIDATE STOCK
        List<OrderItem> domainItems = command.items().stream()
                .map(item -> {
                    // FIXED: Now we can actually find the product in the DB
                    Product product = productRepository.findAll().stream()
                            .filter(p -> p.getSku().equals(item.productName()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Product not found: " + item.productName()));

                    // FIXED: Reduce the stock in the database!
                    product.reduceStock(item.quantity());
                    productRepository.save(product);

                    return new OrderItem(
                            product.getSku(),
                            product.getPrice(), // Use the price from the actual product record
                            item.quantity()
                    );
                })
                .collect(Collectors.toList());

        // 3. Create the aggregate using the factory
        Order order = orderFactory.createOrder(customer, command.shippingAddress(), domainItems);

        // 4. Persist the new order
        orderRepo.save(order);
    }
}