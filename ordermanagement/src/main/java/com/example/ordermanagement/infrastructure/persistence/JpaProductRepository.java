package com.example.ordermanagement.infrastructure.persistence;

import com.example.ordermanagement.domain.model.Product;
import com.example.ordermanagement.domain.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JpaProductRepository implements ProductRepository {

    private final SpringDataProductRepository jpaRepo;

    public JpaProductRepository(SpringDataProductRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public void save(Product product) {
        // Convert Domain -> Entity
        ProductEntity entity = ProductEntity.fromDomain(product);
        jpaRepo.save(entity);
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        // Find Entity -> Convert back to Domain
        return jpaRepo.findBySku(sku).map(ProductEntity::toDomain);
    }

    @Override
    public Optional<Product> findByName(String name) {
        return jpaRepo.findByName(name).map(ProductEntity::toDomain);
    }
    @Override
    public List<Product> findAll() {
        // 1. Get all Entities from the JPA repo
        // 2. Convert each Entity to a Domain Product using .toDomain()
        return jpaRepo.findAll().stream()
                .map(ProductEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> search(String query) {
        // We use a case-insensitive search for better UX
        return jpaRepo.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(query, query)
                .stream()
                .map(ProductEntity::toDomain)
                .collect(Collectors.toList());
    }
}