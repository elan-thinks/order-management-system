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
        Customer customer = customerRepo.findByAuthId(command.authUserId())
                .orElseThrow(() -> new RuntimeException("Customer not registered"));

        List<OrderItem> domainItems = command.items().stream()
                .map(item -> {
                    Product product = productRepository.findByName(item.productName())
                            .orElseThrow(() -> new RuntimeException("Product not found: " + item.productName()));

                    product.reduceStock(item.quantity());
                    productRepository.save(product);

                    // USE THE PRODUCT'S REAL PRICE, NOT THE 0.0 FROM THE COMMAND
                    return new OrderItem(
                            product.getSku(),
                            product.getPrice(), // <--- Critical Fix
                            item.quantity()
                    );
                })
                .collect(Collectors.toList());

        Order order = orderFactory.createOrder(customer, command.shippingAddress(), domainItems);
        orderRepo.save(order);
    }
}