package com.example.ordermanagement.infrastructure.persistence;

import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.repository.OrderRepository;
import org.springframework.stereotype.Repository;
import java.util.stream.Collectors;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaOrderRepository implements OrderRepository {

    private final SpringDataOrderRepository jpaRepository;

    public JpaOrderRepository(SpringDataOrderRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public long count() {
        // This calls the built-in count() method in Spring Data JPA
        return jpaRepository.count();
    }
    @Override
    public void save(Order order) {
        // Map Domain model to JPA Entity before saving
        OrderEntity entity = OrderEntity.fromDomain(order);
        this.jpaRepository.save(entity);
    }

    @Override
    public List<Order> findAll() {
        return jpaRepository.findAll().stream()
                .map(OrderEntity::toDomain) // Map back to Domain for the app
                .collect(Collectors.toList());

    }

    @Override
    public Optional<Order> findById(String id) {
        return jpaRepository.findById(id)
                .map(OrderEntity::toDomain);
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    // infrastructure/persistence/JpaOrderRepository.java
    @Override
    public List<Order> findByProductNameContaining(String term) {
        return jpaRepository.findByProductContainingIgnoreCase(term)
                .stream()
                .map(OrderEntity::toDomain)
                .toList();
    }
    // Implement findById similarly...
}