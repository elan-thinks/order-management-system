package com.example.ordermanagement.domain.repository;

import com.example.ordermanagement.domain.model.Order;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    void save(Order order);
    List<Order> findAll();
    long count();

    // CHANGE: These must be Long to match your implementation
    Optional<Order> findById(Long id);
    void deleteById(Long id);

    // Add this too if you want to find orders by the UUID string from the UI
    Optional<Order> findByPublicId(java.util.UUID publicId);
}