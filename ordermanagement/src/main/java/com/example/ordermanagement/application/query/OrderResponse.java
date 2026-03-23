package com.example.ordermanagement.application.query;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
        String orderId,
        String customerName,
        String shippingAddress,
        List<OrderItemResponse> items, // This must be a List!
        String paymentStatus,
        String orderStatus,
        BigDecimal totalOrderPrice,
        LocalDate orderDate
) {}