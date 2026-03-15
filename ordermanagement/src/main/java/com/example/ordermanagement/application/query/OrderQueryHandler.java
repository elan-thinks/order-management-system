package com.example.ordermanagement.application.query;

import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.value.OrderItem;
import com.example.ordermanagement.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderQueryHandler {
    private final OrderRepository repository;

    public OrderQueryHandler(OrderRepository repository) {
        this.repository = repository;
    }

    public List<OrderResponse> handleAll() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public OrderStatsResponse getStats() {
        List<Order> orders = repository.findAll();

        long totalOrders = orders.size();
        double revenue = orders.stream().mapToDouble(Order::getTotalPrice).sum();
        long pending = orders.stream().filter(o -> o.getStatus().equals("Pending")).count();

        // Calculate total items
        int totalItems = orders.stream()
                .flatMap(o -> o.getItems().stream())
                .mapToInt(OrderItem::getQuantity)
                .sum();

        return new OrderStatsResponse(totalOrders, revenue, pending, totalItems);
    }

    private OrderResponse mapToResponse(Order order) {
        String products = order.getItems().stream()
                .map(OrderItem::getProduct)
                .collect(Collectors.joining(", "));

        double firstItemPrice = order.getItems().isEmpty() ? 0 : order.getItems().get(0).getPrice();
        int totalQty = order.getItems().stream().mapToInt(OrderItem::getQuantity).sum();

        return new OrderResponse(
                order.getId(),
                products,
                firstItemPrice,
                totalQty,
                order.getPayment(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getDate()
        );
    }
}