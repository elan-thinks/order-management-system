package com.example.ordermanagement.infrastructure.persistence;

import com.example.ordermanagement.domain.model.*;
import com.example.ordermanagement.domain.value.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    @Id
    @Column(name = "order_id", length = 50)
    private String id;

    private String authUserId;
    private String customerName;
    private String status;
    private LocalDate createdAt;

    // Address columns stored directly in the order table
    private String street;
    private String city;
    private String zipCode;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderItemEntity> items;

    // Saving: Domain -> Entity
    public static OrderEntity fromDomain(Order order) {
        return new OrderEntity(
                order.getOrderId(),
                order.getCustomer().getAuthId(),
                order.getCustomer().getFullName(),
                order.getStatus().name(),
                order.getCreatedAt(),
                order.getShippingAddress().street(),
                order.getShippingAddress().city(),
                order.getShippingAddress().zipCode(),
                order.getItems().stream().map(OrderItemEntity::fromDomain).collect(Collectors.toList())
        );
    }

    // Loading: Entity -> Domain
    public Order toDomain() {
        Customer customer = new Customer(
                this.authUserId,
                this.customerName,
                new ContactInfo("system@hilcoe.edu.et", "0900000000")
        );

        Address address = new Address(this.street, this.city, this.zipCode);

        List<OrderItem> domainItems = this.items.stream()
                .map(OrderItemEntity::toDomain)
                .collect(Collectors.toList());

        // CALLING THE 6-ARGUMENT CONSTRUCTOR (Fixes your error!)
        return new Order(
                this.id,
                customer,
                address,
                domainItems,
                OrderStatus.valueOf(this.status),
                this.createdAt
        );
    }
}