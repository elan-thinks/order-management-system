package com.example.ordermanagement.domain.value;


import jakarta.persistence.Embeddable;

@Embeddable
public record Address(String street, String city, String zipCode) {
    public Address() { this("", "", ""); } // Dummy no-args for Hibernate
}
