package com.example.ordermanagement.domain.factory;

import com.example.ordermanagement.domain.model.Product;
import com.example.ordermanagement.domain.value.Money;

import java.util.UUID;

public class ProductFactory {
    public static Product create(String sku, String name, Money price, int stock, String category) {
        // Here you can handle UUID generation or complex validation
        int initialStock = Math.max(0, stock);

        return new Product(
                null,
                sku,
                name,
                price,
                initialStock,
                category
        );

    }
}