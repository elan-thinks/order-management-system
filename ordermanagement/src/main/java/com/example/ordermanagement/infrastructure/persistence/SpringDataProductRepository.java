package com.example.ordermanagement.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findBySku(String sku);
    Optional<ProductEntity> findByName(String name);
    List<ProductEntity> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(String name, String sku);
}