package com.example.ordermanagement.domain.value;

public record TrackingCode(String code) {
    public TrackingCode {
        if (code == null || !code.matches("^[A-Z0-9]{12}$")) {
            throw new IllegalArgumentException("Invalid tracking code format");
        }
    }
}