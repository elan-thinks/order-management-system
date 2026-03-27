package com.example.ordermanagement.domain.repository;

import com.example.ordermanagement.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    void save(Product product);
    Optional<Product> findBySku(String sku);
    Optional<Product> findByName(String name);
    List<Product> findAll();
}