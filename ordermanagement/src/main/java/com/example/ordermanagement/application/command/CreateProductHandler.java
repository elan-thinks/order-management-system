package com.example.ordermanagement.application.command;

import com.example.ordermanagement.domain.factory.ProductFactory;
import com.example.ordermanagement.domain.model.Product;
import com.example.ordermanagement.domain.repository.ProductRepository;
import com.example.ordermanagement.domain.value.Money; // Ensure this is imported
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal; // Required for Money

@Service
public class CreateProductHandler {
    private final ProductRepository repository;

    public CreateProductHandler(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void handle(CreateProductCommand command) {
        // 1. Convert the double price into a Money record
        Money price = Money.usd(BigDecimal.valueOf(command.price()));
        if (repository.findBySku(command.sku()).isPresent()) {
            throw new RuntimeException("A product with SKU " + command.sku() + " already exists!");
        }
        // 2. FIXED: Pass 'null' for the ID (the first argument)
        // Required: (Long, String, String, Money, int)
        Product product = ProductFactory.create(
                command.sku(),
                command.name(),
                price,
                command.inventory(), // This matches your 'stockQuantity'
                command.category()
        );

//        product.setStockQuantity(command.inventory());

        repository.save(product);
    }
}