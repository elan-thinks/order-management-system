package com.example.ordermanagement.infrastructure.persistence;

import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.model.OrderItem;
import com.example.ordermanagement.domain.model.Customer;
import com.example.ordermanagement.domain.value.OrderStatus;
import com.example.ordermanagement.domain.value.ContactInfo;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    @Id
    private String id;
    private String authUserId;
    private String customerName;
    private String status;
    private LocalDate createdAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderItemEntity> items;

    // --- FROM DOMAIN (Saving to DB) ---
    public static OrderEntity fromDomain(Order order) {
        return new OrderEntity(
                order.getOrderId(),
                order.getCustomer().getAuthId(), // This saves "user_001"
                order.getCustomer().getFullName(),
                order.getStatus().name(),
                order.getCreatedAt(),
                order.getItems().stream()
                        .map(OrderItemEntity::fromDomain)
                        .collect(Collectors.toList())
        );
    }

    // --- TO DOMAIN (Loading from DB) ---
    // Inside OrderEntity.java

    public Order toDomain() {
        // 1. Reconstruct Customer (Fixed the "n/a" to satisfy validation)
        Customer customer = new Customer(
                this.authUserId != null ? this.authUserId : "unknown",
                this.customerName,
                new ContactInfo("system@hilcoe.edu.et", "000-000-0000") // Use a valid placeholder
        );

        // 2. Reconstruct Items
        List<OrderItem> domainItems = this.items.stream()
                .map(OrderItemEntity::toDomain)
                .collect(Collectors.toList());

        // 3. Rebuild the Order Aggregate
        return new Order(
                this.id,
                customer,
                domainItems,
                OrderStatus.valueOf(this.status),
                this.createdAt
        );
    }
}