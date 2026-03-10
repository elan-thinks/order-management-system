package com.example.ordermanagement.application.query;

import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderQueryHandler {
    private final OrderRepository repository;

    public OrderQueryHandler(OrderRepository repository) {
        this.repository = repository;
    }

    // Fetches all orders for the table
    public List<OrderResponse> handleAll() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }
    // ADD THIS METHOD BELOW
    private OrderResponse mapToResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getProduct(),
                order.getQuantity(),
                order.getPrice(),
                order.getQuantity() * order.getPrice(),
                order.getPayment(),
                order.getStatus()
        );
    }

    // Calculates the KPI card data
    public OrderStatsResponse getStats() {
        var orders = repository.findAll();
        double revenue = orders.stream().mapToDouble(o -> o.getQuantity() * o.getPrice()).sum();
        int items = orders.stream().mapToInt(o -> o.getQuantity()).sum();
        return new OrderStatsResponse(orders.size(), revenue, items);
    }

    public List<OrderResponse> handle(GetOrderSummaryQuery query) {
        return repository.findAll().stream()
                .map(order -> new OrderResponse(
                        order.getId(),           // 1. String id
                        order.getProduct(),      // 2. String productName
                        order.getQuantity(),     // 3. int qty
                        order.getPrice(),        // 4. double unitPrice
                        order.getQuantity() * order.getPrice(), // 5. double total
                        order.getPayment(),      // 6. String payment (NEW)
                        order.getStatus()        // 7. String orderStatus (NEW)
                ))
                .toList();
    }

    // application/query/OrderQueryHandler.java
    public List<OrderResponse> search(String term) {
        return repository.findByProductNameContaining(term).stream()
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getProduct(),
                        order.getQuantity(),
                        order.getPrice(),
                        order.getQuantity() * order.getPrice(),
                        order.getPayment(),
                        order.getStatus()
                ))
                .toList();
    }
    public OrderResponse getById(String id) { // Change UUID to String here
        return repository.findById(id)
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getProduct(),
                        order.getQuantity(),
                        order.getPrice(),
                        order.getQuantity() * order.getPrice(),
                        order.getPayment(), // New parameter 6
                        order.getStatus()   // New parameter 7
                ))
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
    }
}
