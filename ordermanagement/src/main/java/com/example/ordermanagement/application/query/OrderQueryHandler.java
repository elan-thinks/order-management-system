package com.example.ordermanagement.application.query;

import com.example.ordermanagement.domain.model.Product;
import com.example.ordermanagement.domain.repository.OrderRepository;
import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.repository.ProductRepository;
import com.example.ordermanagement.domain.value.OrderStatus;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderQueryHandler {

    private final OrderRepository repository;
    private final ProductRepository productRepository; // Added this

    public OrderQueryHandler(OrderRepository repository, ProductRepository productRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
    }

    public List<OrderResponse> handle(GetOrdersQuery query) {
        return repository.findAll().stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    // THIS IS THE MISSING METHOD CAUSING THE ERROR
    public OrderStatsResponse getStats() {
        List<Order> allOrders = repository.findAll();
        List<Product> allProducts = productRepository.findAll(); // Get products for stock alerts

        long totalOrders = allOrders.size();

        // 1. Calculate Total Revenue from DELIVERED orders (Accounting Best Practice)
        BigDecimal totalRevenue = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .map(order -> order.calculateSubtotal().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long pendingCount = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.PENDING).count();
        long paidCount = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.PAID).count();
        long shippedCount = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.SHIPPED).count();
        long deliveredCount = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.DELIVERED).count();
        long cancelledCount = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.CANCELLED).count();

        // 3. Logic for the Dashboard Cards
        long activeListings = allProducts.size();
        long lowStockCount = allProducts.stream()
                .filter(p -> p.getStockQuantity() < 5) // Assuming 5 is your threshold
                .count();

        return new OrderStatsResponse(
                allOrders.size(),
                totalRevenue,
                pendingCount,
                0, // paidCount (if not used)
                shippedCount,
                deliveredCount,
                cancelledCount,
                activeListings, // New field
                lowStockCount   // New field
        );
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getOrderId(),
                order.getCustomer().getFullName(),
                order.getShippingAddress().street() + ", " + order.getShippingAddress().city(),
                order.getItems().stream()
                        .map(OrderItemResponse::fromDomain)
                        .collect(Collectors.toList()),
                order.getPaymentStatus(),
                order.getStatus().name(),
                order.calculateSubtotal().amount(),
                order.getCreatedAt()
        );
    }
}