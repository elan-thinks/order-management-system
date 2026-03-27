package com.example.ordermanagement.application.query;

import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.repository.CustomerRepository;
import com.example.ordermanagement.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetOrdersHandler { // Name matches the file name

    private final OrderRepository repository;
    private final CustomerRepository customerRepository;

    public GetOrdersHandler(OrderRepository repository, CustomerRepository customerRepository) {
        this.repository = repository;
        this.customerRepository = customerRepository;
    }

    public List<OrderResponse> handle(GetOrdersQuery query) {
        return repository.findAll().stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    // Inside GetOrdersHandler.java mapping method
    private OrderResponse mapToOrderResponse(Order order) {
        String name = customerRepository.findById(order.getCustomerId())
                .map(c -> c.getFullName())
                .orElse("Unknown");
        return new OrderResponse(
                order.getOrderId().toString(),
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