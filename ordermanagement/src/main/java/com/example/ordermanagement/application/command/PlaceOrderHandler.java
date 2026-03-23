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
    private final OrderRepository orderRepo;
    private final CustomerRepository customerRepo;
    private final ProductRepository productRepository;
    private final OrderFactory orderFactory;

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
        // 1. Find the Customer
        Customer customer = customerRepo.findByAuthId(command.authUserId())
                .orElseThrow(() -> new RuntimeException("Customer not registered"));

        List<OrderItem> domainItems = new ArrayList<>();

        // 2. Process each item (Update stock and prepare domain items)
        for (var itemData : command.items()) {
            // Find product by name to get authoritative SKU and Price
            Product product = productRepository.findByName(itemData.productName())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemData.productName()));

            // Logic check: Verify stock availability
            if (product.getStockQuantity() < itemData.quantity()) {
                throw new IllegalStateException("Not enough stock for " + product.getName());
            }

            // Deduct stock from the Product aggregate
            product.reduceStock(itemData.quantity());
            // Persist the stock change back to MySQL
            productRepository.save(product);

            // Create the Domain item using the Product's SKU and Price from the DB
            domainItems.add(new OrderItem(
                    product.getSku(),
                    product.getPrice(),
                    itemData.quantity()
            ));
        }

        // 3. Create the Order using the Factory
        Order order = orderFactory.createOrder(customer, command.shippingAddress(), domainItems);

        // 4. Save the final Order
        orderRepo.save(order);
    }
}