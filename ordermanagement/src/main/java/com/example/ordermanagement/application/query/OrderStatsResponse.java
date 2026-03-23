package com.example.ordermanagement.application.query;

import java.math.BigDecimal;

public record OrderStatsResponse(
        long totalOrders,
        BigDecimal totalRevenue,
        long newOrders,        // pendingCount
        long processingOrders, // paidCount
        long shippedOrders,
        long deliveredOrders,
        long cancelledOrders,
        long activeListings,   // Added for Dashboard
        long lowStockCount     // Added for Dashboard
) {}