package com.example.ordermanagement.application.query;

public record OrderStatsResponse(
        long totalOrders,
        double revenue,
        long pendingOrders,
        int totalItems
) {}