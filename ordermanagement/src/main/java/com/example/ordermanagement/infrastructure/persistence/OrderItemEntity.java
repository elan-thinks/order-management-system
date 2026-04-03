package com.example.ordermanagement.infrastructure.persistence;

import com.example.ordermanagement.domain.model.OrderItem;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
public class OrderItemEntity {

    @Id
    @Column(length = 36)
    private String id;

    private String sku;
    private BigDecimal unitPrice;
    private int quantity;


    public OrderItemEntity(String id, String sku, BigDecimal unitPrice, int quantity) {
        this.id = id;
        this.sku = sku;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public static OrderItemEntity fromDomain(OrderItem item) {
        return new OrderItemEntity(
                UUID.randomUUID().toString(),
                item.getSku(),
                item.getUnitPrice().amount(),
                item.getQuantity()
        );
    }

    public OrderItem toDomain() {
        return new OrderItem(
                this.sku,
                com.example.ordermanagement.domain.value.Money.usd(this.unitPrice),
                this.quantity
        );
    }
}