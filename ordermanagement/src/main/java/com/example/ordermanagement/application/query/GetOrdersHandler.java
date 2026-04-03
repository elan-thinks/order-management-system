package com.example.ordermanagement.application.query;

import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.repository.CustomerRepository;
import com.example.ordermanagement.domain.repository.OrderReadRepository;
import com.example.ordermanagement.domain.repository.OrderWriteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetOrdersHandler {

    private final OrderReadRepository readRepository;
    private final CustomerRepository customerRepository;

    public GetOrdersHandler(OrderReadRepository readRepository, CustomerRepository customerRepository) {
        this.readRepository = readRepository;
        this.customerRepository = customerRepository;
    }

    public List<OrderResponse> handle(GetOrdersQuery query) {
        String searchTerm = query.searchTerm();

        return readRepository.findAll().stream()
                .filter(order -> {
                    if (searchTerm == null || searchTerm.isEmpty()) return true;

                    // Search by Order ID or look up customer name to filter
                    String customerName = customerRepository.findById(order.getCustomerId())
                            .map(c -> c.getFullName().toLowerCase())
                            .orElse("");

                    return order.getOrderId().toString().contains(searchTerm) ||
                            customerName.contains(searchTerm.toLowerCase());
                })
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    // Inside GetOrdersHandler.java mapping method
    private OrderResponse mapToOrderResponse(Order order) {
        String name = customerRepository.findById(order.getCustomerId())
                .map(c -> c.getFullName())
                .orElse("Unknown");

        // NULL SAFE ADDRESS CHECK
        String addressStr = "No Address";
        if (order.getShippingAddress() != null) {
            addressStr = order.getShippingAddress().street() + ", " + order.getShippingAddress().city();
        }

        return new OrderResponse(
                order.getPublicId().toString(),
                name,
                addressStr,
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