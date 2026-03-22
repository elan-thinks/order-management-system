package com.example.ordermanagement.application.query;

import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.model.OrderItem;
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
                .flatMap(this::mapToResponses) // Flattening so each item is a row
                .collect(Collectors.toList());
    }

    private java.util.stream.Stream<OrderResponse> mapToResponses(Order order) {
        return order.getItems().stream().map(item -> new OrderResponse(
                order.getOrderId().toString(),
                order.getCustomer().getFullName(),
                item.getSku(),                           // Now returns String
                item.getUnitPrice().amount(),
                item.getQuantity(),                      // Now returns int
                order.getPaymentStatus(),                // Now returns String ("Paid"/"Unpaid")
                order.getStatus().name(),
                item.getSubtotal().amount(),
                order.getCreatedAt()                     // Now returns LocalDate
        ));

    }
}