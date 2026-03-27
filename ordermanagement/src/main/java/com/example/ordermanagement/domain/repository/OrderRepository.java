package com.example.ordermanagement.domain.repository;

import com.example.ordermanagement.domain.model.Order;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    void save(Order order);
    List<Order> findAll();
    long count();
    Optional<Order> findByPublicId(UUID publicId);
    // CHANGE: These must be Long to match your implementation
    Optional<Order> findById(Long id);
    void deleteById(Long id);
}