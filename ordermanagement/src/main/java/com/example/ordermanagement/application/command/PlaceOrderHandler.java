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
        System.out.println("DEBUG: Starting PlaceOrder for customer: " + command.authUserId());

        // 1. Find the Customer
        Customer customer = customerRepo.findByAuthId(command.authUserId())
                .orElseThrow(() -> new RuntimeException("Customer not registered: " + command.authUserId()));
        System.out.println("DEBUG: Found Customer ID: " + customer.getId());

        List<OrderItem> domainItems = new ArrayList<>();

        for (var itemData : command.items()) {
            System.out.println("DEBUG: Looking for product: " + itemData.productName());

            Product product = productRepository.findBySku(itemData.productName())
                    .or(() -> productRepository.findByName(itemData.productName()))
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemData.productName()));

            System.out.println("DEBUG: Found Product! Current Stock: " + product.getStockQuantity());

            product.reduceStock(itemData.quantity());
            productRepository.save(product);

            domainItems.add(new OrderItem(
                    product.getSku(),
                    product.getPrice(),
                    itemData.quantity()
            ));
        }

        // 3. Create the Order
        System.out.println("DEBUG: Calling OrderFactory...");
        Order order = orderFactory.createOrder(customer.getId(), command.shippingAddress(), domainItems);

        System.out.println("DEBUG: Order created with PublicID: " + order.getPublicId());

        // 4. Save the final Order
        orderRepo.save(order);
        System.out.println("DEBUG: Order saved successfully!");
    }
}