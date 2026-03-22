package com.example.ordermanagement.application.query;

import java.math.BigDecimal;

public record OrderStatsResponse(
        long totalOrders,
        BigDecimal totalRevenue,
        long newOrders,
        long processingOrders,
        long shippedOrders,
        long deliveredOrders,
        long cancelledOrders
) {}