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
    private String id;
    private String sku;
    private BigDecimal unitPrice;
    private int quantity;

    public static OrderItemEntity fromDomain(OrderItem item) {
        return new OrderItemEntity(
                java.util.UUID.randomUUID().toString(),
                item.getSku(),
                item.getUnitPrice().amount(),
                item.getQuantity()
        );
    }

    // --- TO DOMAIN (Loading from DB) ---
    public OrderItem toDomain() {
        return new OrderItem(
                this.sku,
                // Use the usd() helper to fix the constructor length error
                Money.usd(this.unitPrice),
                this.quantity
        );
    }
}