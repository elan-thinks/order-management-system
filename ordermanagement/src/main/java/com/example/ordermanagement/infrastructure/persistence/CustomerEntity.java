package com.example.ordermanagement.infrastructure.persistence;

import com.example.ordermanagement.domain.model.Customer;
import com.example.ordermanagement.domain.value.ContactInfo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String authId;
    private String fullName;

    // Convert Domain -> Entity
    public static CustomerEntity fromDomain(Customer customer) {
        CustomerEntity entity = new CustomerEntity();
        entity.setId(customer.getId());
        entity.setAuthId(customer.getAuthId());
        entity.setFullName(customer.getFullName());
        return entity;
    }

    // Convert Entity -> Domain
    public Customer toDomain() {
        return new Customer(
                this.id,
                this.authId,
                this.fullName,
                new ContactInfo("email@example.com", "0900000000") // Or map real fields if you added them
        );
    }
}