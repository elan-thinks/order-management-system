package com.example.ordermanagement.application.query;

import com.example.ordermanagement.domain.model.Product;
import com.example.ordermanagement.domain.repository.CustomerRepository;
import com.example.ordermanagement.domain.repository.OrderRepository;
import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.repository.ProductRepository;
import com.example.ordermanagement.domain.value.OrderStatus;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderQueryHandler {

    private final OrderRepository repository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public OrderQueryHandler(OrderRepository repository, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    public List<OrderResponse> handle(GetOrdersQuery query) {
        return repository.findAll().stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    public DashboardStats getStats() {
        List<Order> allOrders = repository.findAll();
        List<Product> allProducts = productRepository.findAll();

        // 1. Calculate Total Revenue (Only from Completed/Delivered orders)
        BigDecimal totalRevenue = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .map(order -> order.calculateSubtotal().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2. Inventory Stats
        long activeListings = allProducts.size();
        long lowStockCount = allProducts.stream()
                .filter(p -> p.getStockQuantity() < 5)
                .count();

        // 3. Status Counts (Matching your orders.html exactly)
        long newOrders = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.PENDING).count();

        long processingOrders = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.PAID).count();

        long shippedOrders = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.SHIPPED).count();

        long deliveredOrders = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED).count();

        long cancelledOrders = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.CANCELLED).count();

        // 4. Chart Data
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(6);
        List<BigDecimal> weeklySales = repository.getSalesForLast7Days(sevenDaysAgo);

        // Ensure we don't pass a null list to JavaScript
        if (weeklySales == null) weeklySales = List.of();

        return new DashboardStats(
                totalRevenue,
                activeListings,
                newOrders,
                processingOrders,
                shippedOrders,
                deliveredOrders,
                cancelledOrders,
                lowStockCount,
                weeklySales
        );
    }

    private OrderResponse mapToOrderResponse(Order order) {
        String name = customerRepository.findById(order.getCustomerId())
                .map(customer -> customer.getFullName())
                .orElse("Customer #" + order.getCustomerId());

        return new OrderResponse(
                String.valueOf(order.getPublicId()),
                name,
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