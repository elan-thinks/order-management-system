package com.example.ordermanagement.domain.factory;

import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.model.Customer;
import com.example.ordermanagement.domain.model.OrderItem;
import com.example.ordermanagement.domain.value.Address;
import com.example.ordermanagement.domain.value.OrderStatus;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class OrderFactory {

    // Your original ID logic, but cleaner
    private String generateFormattedId() {
        return String.format("ORD-%d", System.currentTimeMillis() % 100000);
    }

    public Order createOrder(Customer customer, Address shippingAddress, List<OrderItem> items) {
        String newId = generateFormattedId(); // Returns "ORD-XXXXX"
        Order order = new Order(newId, customer, shippingAddress);

        for (OrderItem item : items) {
            order.addItem(item);
        }
        return order;
    }
}