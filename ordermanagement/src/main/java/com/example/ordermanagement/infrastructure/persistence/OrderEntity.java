package com.example.ordermanagement.infrastructure.persistence;

import com.example.ordermanagement.domain.model.*;
import com.example.ordermanagement.domain.value.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id") // This ensures your PK is called order_id in the DB
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID publicId; // Hibernate handles UUIDs automatically!

    private Long customerId; // Matches Rule 2
    private String status;
    private LocalDate createdAt;
    private BigDecimal amount; // <--- This is the name Hibernate knows
//    private LocalDateTime createdAt; // <--- This is the name Hibernate knows

    private String street;
    private String city;
    private String zipCode;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id") // Foreign key in order_items table
    private List<OrderItemEntity> items;

    public static OrderEntity fromDomain(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getOrderId());
        entity.setPublicId(order.getPublicId());
        entity.setCustomerId(order.getCustomerId());
        entity.setStatus(order.getStatus().name());
        entity.setCreatedAt(order.getCreatedAt());
        entity.setStreet(order.getShippingAddress().street());
        entity.setCity(order.getShippingAddress().city());
        entity.setZipCode(order.getShippingAddress().zipCode());
        entity.setItems(order.getItems().stream()
                .map(OrderItemEntity::fromDomain)
                .collect(Collectors.toList()));
        return entity;
    }

    public Order toDomain() {
        Address address = new Address(this.street, this.city, this.zipCode);
        List<OrderItem> domainItems = this.items.stream()
                .map(OrderItemEntity::toDomain)
                .collect(Collectors.toList());

        return new Order(
                this.id,
                this.publicId,
                this.customerId,
                address,
                domainItems,
                OrderStatus.valueOf(this.status),
                this.createdAt
        );
    }
}