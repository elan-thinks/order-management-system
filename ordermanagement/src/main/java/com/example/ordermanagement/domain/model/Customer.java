package com.example.ordermanagement.domain.model;

import com.example.ordermanagement.domain.value.ContactInfo;

public class Customer {
    private Long id; // Pure Long ID
    private String authId;
    private String fullName;
    private ContactInfo contactInfo;

    public Customer(Long id, String authId, String fullName, ContactInfo contactInfo) {
        this.id = id;
        this.authId = authId;
        this.fullName = fullName;
        this.contactInfo = contactInfo;
    }

    // Getters
    public Long getId() { return id; }
    public String getAuthId() { return authId; }
    public String getFullName() { return fullName; }
    public ContactInfo getContactInfo() { return contactInfo; }
}