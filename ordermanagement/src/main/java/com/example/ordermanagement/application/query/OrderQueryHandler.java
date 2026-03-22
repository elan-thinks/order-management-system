package com.example.ordermanagement.application.query;

import com.example.ordermanagement.domain.repository.OrderRepository;
import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.repository.ProductRepository;
import com.example.ordermanagement.domain.value.OrderStatus;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
//@Service
public class OrderQueryHandler {

    private final OrderRepository repository;
    private final ProductRepository productRepository; // 1. Add this

    public OrderQueryHandler(OrderRepository repository, ProductRepository productRepository) {
        this.repository = repository;
        this.productRepository = productRepository; // 2. Initialize it
    }

    // Inside OrderQueryHandler.java
    public List<OrderResponse> handle(GetOrdersQuery query) {
        return repository.findAll().stream()
                .flatMap(order -> order.getItems().stream().map(item -> new OrderResponse(
                        order.getOrderId(),
                        order.getCustomer().getFullName(),
                        item.getSku(),
                        item.getUnitPrice().amount(),
                        item.getQuantity(),
                        order.getPaymentStatus(),
                        order.getStatus().name(),
                        order.calculateSubtotal().amount(), // 👈 Total for the dashboard
                        order.getCreatedAt()               // 👈 Date for the table
                )))
                .collect(Collectors.toList());
    }

    public OrderStatsResponse getStats() {
        List<Order> allOrders = repository.findAll();

        // 1. Collect all the data points
        long totalOrders = allOrders.size(); // long (Position 1)

        BigDecimal totalRevenue = allOrders.stream()
                .map(order -> order.calculateSubtotal().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add); // BigDecimal (Position 2)

        long newCount = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.PENDING).count(); // long (Position 3)
        long processingCount = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.PAID).count(); // long (Position 4)
        long shippedCount = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.SHIPPED).count(); // long (Position 5)
        long deliveredCount = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.DELIVERED).count(); // long (Position 6)
        long cancelledCount = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.CANCELLED).count(); // long (Position 7)

        // 2. Return them in the EXACT order the record requires
        return new OrderStatsResponse(
                totalOrders,      // 1
                totalRevenue,    // 2
                newCount,        // 3
                processingCount, // 4
                shippedCount,    // 5
                deliveredCount,  // 6
                cancelledCount   // 7
        );
    }
}