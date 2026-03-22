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
        // 1. Convert the double price into a Money record using the .usd() helper
        // Since your record requires (BigDecimal, Currency), .usd() handles the Currency for you.
        Money price = Money.usd(BigDecimal.valueOf(command.price()));

        // 2. Use the strict constructor required by your Product model
        // Order: (String name, String sku, Money price, int inventory)
        Product product = new Product(
                command.name(),
                command.sku(),
                price,
                command.inventory()
        );

        // 3. Set additional fields that aren't in the constructor
        product.setCategory(command.category());
        product.setStatus("Active");

        repository.save(product);
    }
}