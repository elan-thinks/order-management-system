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
    @Column(name = "order_id") // Add this to ensure it maps to order_id, not id
    private String id;

    private String authUserId;
    private String customerName;
    private String status;
    private LocalDate createdAt;

    // ADDED: Shipping Address Columns
    private String street;
    private String city;
    private String zipCode;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderItemEntity> items;

    // --- FROM DOMAIN (Saving to DB) ---
    public static OrderEntity fromDomain(Order order) {
        return new OrderEntity(
                order.getOrderId(),
                order.getCustomer().getAuthId(),
                order.getCustomer().getFullName(),
                order.getStatus().name(),
                order.getCreatedAt(),
                // MAP THE ADDRESS HERE
                order.getShippingAddress().street(),
                order.getShippingAddress().city(),
                order.getShippingAddress().zipCode(),
                order.getItems().stream()
                        .map(OrderItemEntity::fromDomain)
                        .collect(Collectors.toList())
        );
    }

    // --- TO DOMAIN (Loading from DB) ---
    public Order toDomain() {
        Customer customer = new Customer(
                this.authUserId != null ? this.authUserId : "unknown",
                this.customerName,
                new ContactInfo("system@hilcoe.edu.et", "000-000-0000")
        );

        List<OrderItem> domainItems = this.items.stream()
                .map(OrderItemEntity::toDomain)
                .collect(Collectors.toList());

        // Create the Address value object from DB columns
        Address address = new Address(this.street, this.city, this.zipCode);

        return new Order(
                this.id,
                customer,
                address, // Pass the reconstructed address
                domainItems,
                OrderStatus.valueOf(this.status),
                this.createdAt
        );
    }
}