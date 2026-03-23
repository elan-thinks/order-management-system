package com.example.ordermanagement.application.query;

import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetOrdersHandler { // Name matches the file name

    private final OrderRepository repository;

    public GetOrdersHandler(OrderRepository repository) {
        this.repository = repository;
    }

    public List<OrderResponse> handle(GetOrdersQuery query) {
        return repository.findAll().stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    // Inside GetOrdersHandler.java mapping method
    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getOrderId(),
                order.getCustomer().getFullName(),
                order.getShippingAddress().street() + ", " + order.getShippingAddress().city(),
                order.getItems().stream()
                        .map(OrderItemResponse::fromDomain)
                        .collect(Collectors.toList()),
                order.getPaymentStatus(), // <--- This will now work!
                order.getStatus().name(),
                order.calculateSubtotal().amount(),
                order.getCreatedAt()
        );
    }
}