package com.example.ordermanagement.domain.repository;

import com.example.ordermanagement.domain.model.Order;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    void save(Order order);
    List<Order> findAll();
    long count();
    Optional<Order> findById(String id);
    void deleteById(String id);
}