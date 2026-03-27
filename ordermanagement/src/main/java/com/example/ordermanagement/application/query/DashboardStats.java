package com.example.ordermanagement.application.query;

import java.math.BigDecimal;
import java.util.List;

public record DashboardStats(
        java.math.BigDecimal totalRevenue,
        long activeListings,
        long newOrders,
        long processingOrders,
        long shippedOrders,
        long deliveredOrders,
        long cancelledOrders,
        long lowStockCount,
        java.util.List<java.math.BigDecimal> weeklySales
) {}
