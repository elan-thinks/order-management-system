package com.example.ordermanagement.infrastructure.persistence;

import com.example.ordermanagement.domain.model.OrderItem;
import com.example.ordermanagement.domain.value.Money;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Let Hibernate handle the ID generation
    private String id;

    private String sku;
    private BigDecimal unitPrice;
    private int quantity;

    public static OrderItemEntity fromDomain(OrderItem item) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setSku(item.getSku());
        entity.setUnitPrice(item.getUnitPrice().amount());
        entity.setQuantity(item.getQuantity());
        return entity;
    }

    public OrderItem toDomain() {
        return new OrderItem(
                this.sku,
                Money.usd(this.unitPrice),
                this.quantity
        );
    }
}