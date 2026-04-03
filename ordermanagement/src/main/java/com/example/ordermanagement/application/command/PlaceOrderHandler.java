package com.example.ordermanagement.application.command;

import com.example.ordermanagement.domain.factory.OrderFactory;
import com.example.ordermanagement.domain.model.*;
import com.example.ordermanagement.domain.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.ArrayList;

@Service
public class PlaceOrderHandler {
    private final OrderWriteRepository orderWriteRepo;
    private final CustomerRepository customerRepo;
    private final ProductRepository productRepository;
    private final OrderFactory orderFactory;

    public PlaceOrderHandler(OrderWriteRepository orderWriteRepo,
                             CustomerRepository customerRepo,
                             ProductRepository productRepository,
                             OrderFactory orderFactory) {
        this.orderWriteRepo = orderWriteRepo;
        this.customerRepo = customerRepo;
        this.productRepository = productRepository;
        this.orderFactory = orderFactory;
    }

    @Transactional
    public void handle(PlaceOrderCommand command) {
        System.out.println("DEBUG: Starting PlaceOrder for customer: " + command.authUserId());

        // 1. Find the Customer
        Customer customer = customerRepo.findByAuthId(command.authUserId())
                .orElseThrow(() -> new RuntimeException("Customer not registered: " + command.authUserId()));

        // 2. Find the single Product directly from the command
        Product product = productRepository.findBySku(command.productName())
                .or(() -> productRepository.findByName(command.productName()))
                .orElseThrow(() -> new RuntimeException("Product not found: " + command.productName()));

        System.out.println("DEBUG: Found Product: " + product.getName() + " | Stock: " + product.getStockQuantity());

        // 3. Logic for stock and creating domain items
        product.reduceStock(command.qty()); // This is where the "Not enough stock" check happens
        productRepository.save(product);

        // We create a list with just this one item to satisfy the OrderFactory
        List<OrderItem> domainItems = List.of(new OrderItem(
                product.getSku(),
                product.getPrice(),
                command.qty()
        ));

        // 4. Create and Save the Order
        System.out.println("DEBUG: Calling OrderFactory...");
        Order order = orderFactory.createOrder(customer.getId(), command.shippingAddress(), domainItems);

        orderWriteRepo.save(order);
        System.out.println("DEBUG: Order saved successfully with PublicID: " + order.getPublicId());
    }
}