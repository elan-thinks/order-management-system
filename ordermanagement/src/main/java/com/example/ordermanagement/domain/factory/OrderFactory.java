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

    private String generateFormattedId() {
        return String.format("ORD-%d", System.currentTimeMillis() % 100000);
    }

    public Order createOrder(Long customerId, Address shippingAddress, List<OrderItem> items) {

        Order order = new Order(customerId, shippingAddress);

        for (OrderItem item : items) {
            order.addItem(item);
        }
        return order;
    }
}