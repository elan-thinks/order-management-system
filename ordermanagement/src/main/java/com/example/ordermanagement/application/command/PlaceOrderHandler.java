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
            Product product = productRepository.findByName(itemData.productName())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemData.productName()));

            // Logic: Deduct stock from the Product aggregate
            product.reduceStock(itemData.quantity());

            // Persist the stock change
            productRepository.save(product);

            // Create the Domain item using the Product's authoritative SKU and Price
            domainItems.add(new OrderItem(
                    product.getSku(),
                    product.getPrice(),
                    itemData.quantity()
            ));
        }

        // 3. Create the Order using the Factory (keeps ID generation logic centralized)
        Order order = orderFactory.createOrder(customer, command.shippingAddress(), domainItems);

        // 4. Save the final Order (and its items via cascade)
        orderRepo.save(order);
    }
}