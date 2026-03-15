package com.example.ordermanagement.application.query;

import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.value.OrderItem;
import com.example.ordermanagement.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetOrdersHandler {

    private final OrderRepository repository;

    public GetOrdersHandler(OrderRepository repository) {
        this.repository = repository;
    }

    public List<OrderResponse> handle() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private OrderResponse mapToResponse(Order order) {
        String productNames = order.getItems().stream()
                .map(OrderItem::getProduct)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        OrderItem firstItem = order.getItems().isEmpty()
                ? new OrderItem("", 0, new com.example.ordermanagement.domain.value.Money(0.0))
                : order.getItems().get(0);

        double unitPrice = firstItem.getPrice();
        double totalPrice = order.getTotalPrice();

        return new OrderResponse(
                order.getId(),
                productNames,
                unitPrice,
                firstItem.getQuantity(),
                order.getPayment(),
                order.getStatus(),
                totalPrice,
                order.getDate()
        );
    }
}
