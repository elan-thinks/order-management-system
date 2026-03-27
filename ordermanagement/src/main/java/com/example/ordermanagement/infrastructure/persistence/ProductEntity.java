package com.example.ordermanagement.infrastructure.persistence;


import com.example.ordermanagement.domain.model.Product;
import com.example.ordermanagement.domain.value.Money;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String sku;
    private String name;
    private int stockQuantity;
    private String category;

    @Embedded
    private Money price;

    public static ProductEntity fromDomain(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setSku(product.getSku());
        entity.setName(product.getName());
        entity.setStockQuantity(product.getStockQuantity());
        entity.setPrice(product.getPrice());
        entity.setCategory(product.getCategory());
        return entity;
    }

    public Product toDomain() {
        return new Product(this.id, this.sku, this.name, this.price, this.stockQuantity,this.category);
    }
}