package com.example.ordermanagement.application.command;

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

        // 2. FIXED: Pass 'null' for the ID (the first argument)
        // Required: (Long, String, String, Money, int)
        Product product = new Product(
                null,           // <--- The missing Long ID
                command.sku(),
                command.name(),
                price,
                command.inventory()
        );

        // 3. Optional: If your Product model has these setters
        product.setStockQuantity(command.inventory());

        repository.save(product);
    }
}