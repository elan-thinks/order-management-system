package com.example.ordermanagement.domain.value;

import jakarta.persistence.Embeddable;

@Embeddable // This tells JPA to flatten these fields into the Customer table
public record ContactInfo(String email, String phone) {
    public ContactInfo {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid Email");
        }
    }

    // Hibernate needs this "no-args" simulation for records
    // If you get a "no default constructor" error, you may need to
    // change this record to a Class with @NoArgsConstructor.
    public ContactInfo() {
        this("unknown@example.com", "0000000000");
    }
}