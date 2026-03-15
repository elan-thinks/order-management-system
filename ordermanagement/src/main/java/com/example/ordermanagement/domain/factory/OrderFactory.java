package com.example.ordermanagement.domain.factory;

import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.repository.OrderRepository;
import com.example.ordermanagement.domain.value.OrderItem;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

@Component
public class OrderFactory {
    private final OrderRepository repository;

    public OrderFactory(OrderRepository repository) {
        this.repository = repository;
    }

    public Order createOrder(List<OrderItem> items) {
        String formattedId = String.format("ORD-%d", System.currentTimeMillis() % 100000);

        // Initializing with "Unpaid" so it only becomes "Paid" when "Done" is clicked
        Order order = new Order(formattedId, "Pending", "Unpaid", LocalDate.now());

        // Use a standard lambda instead of a method reference
        for (OrderItem item : items) {
            order.addItem(item);
        }

        // Calculate total quantity and set it
        int totalQty = items.stream().mapToInt(OrderItem::getQuantity).sum();
        order.setQuantity(totalQty);

        return order;
    }
}