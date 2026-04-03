package com.example.ordermanagement.domain.repository;

import com.example.ordermanagement.domain.model.Order;

import java.util.UUID;

public interface OrderWriteRepository {
    void save(Order order);
    void deleteById(Long id);
}
