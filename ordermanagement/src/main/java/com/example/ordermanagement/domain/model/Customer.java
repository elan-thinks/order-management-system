package com.example.ordermanagement.domain.model;

import com.example.ordermanagement.domain.value.ContactInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@Getter
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // The REAL Primary Key for MySQL

    @Column(unique = true, nullable = false)
    private String authId; // This is your "user_001" String

    private String fullName;

    @Embedded
    private ContactInfo contactInfo;

    // Standard constructor for your DataInitializer
    public Customer(String authId, String fullName, ContactInfo contactInfo) {
        this.authId = authId;
        this.fullName = fullName;
        this.contactInfo = contactInfo;
    }
}