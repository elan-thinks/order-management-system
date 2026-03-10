package com.example.ordermanagement.domain.repository;

import com.example.ordermanagement.domain.model.Order;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    void save(Order order);
    List<Order> findAll();
    long count();
    // Ensure this uses String, not UUID!
    Optional<Order> findById(String id);
    void deleteById(String id);
    // domain/repository/OrderRepository.java
    List<Order> findByProductNameContaining(String term);
}