package com.example.ordermanagement.application.query;

import com.example.ordermanagement.domain.repository.OrderRepository;
import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.value.OrderStatus;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderQueryHandler {

    private final OrderRepository repository;

    public OrderQueryHandler(OrderRepository repository) {
        this.repository = repository;
    }

    public List<OrderResponse> handle(GetOrdersQuery query) {
        return repository.findAll().stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    // THIS IS THE MISSING METHOD CAUSING THE ERROR
    public OrderStatsResponse getStats() {
        List<Order> allOrders = repository.findAll();

        long totalOrders = allOrders.size();

        BigDecimal totalRevenue = allOrders.stream()
                .map(order -> order.calculateSubtotal().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long pendingCount = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.PENDING).count();
        long paidCount = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.PAID).count();
        long shippedCount = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.SHIPPED).count();
        long deliveredCount = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.DELIVERED).count();
        long cancelledCount = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.CANCELLED).count();

        return new OrderStatsResponse(
                totalOrders,
                totalRevenue,
                pendingCount,
                paidCount,
                shippedCount,
                deliveredCount,
                cancelledCount
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