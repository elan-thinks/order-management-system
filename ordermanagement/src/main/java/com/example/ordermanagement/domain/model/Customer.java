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
    private Long id; // This will be the ONLY primary key

    @Column(unique = true, nullable = false)
    private String authId; // Keep this for your login logic, but it's not the @Id

    private String fullName;

    @Embedded
    private ContactInfo contactInfo;

    public Customer(String authId, String fullName, ContactInfo contactInfo) {
        this.authId = authId;
        this.fullName = fullName;
        this.contactInfo = contactInfo;
    }
}