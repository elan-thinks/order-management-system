package com.example.ordermanagement.application.query;

public record OrderStatsResponse(
        long totalOrders,
        double totalRevenue,
        int totalItemsSold
) {}