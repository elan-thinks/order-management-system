package com.example.ordermanagement.domain.repository;

import com.example.ordermanagement.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // This tells Spring to create a "Bean" for this interface
public interface ProductRepository extends JpaRepository<Product, Long> {
    // You can add custom search methods here later
    java.util.Optional<Product> findByName(String name);
}