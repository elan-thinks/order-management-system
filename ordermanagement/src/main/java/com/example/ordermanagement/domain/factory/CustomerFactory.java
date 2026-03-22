package com.example.ordermanagement.domain.factory;

import com.example.ordermanagement.domain.model.Customer;
import com.example.ordermanagement.domain.value.ContactInfo;

public class CustomerFactory {

    /**
     * Assembles a new Customer entity.
     * @param authId The ID from your Login/Security system (e.g., "user_123" or email)
     * @param name The customer's full name
     * @param email The email for business contact
     * @param phone The phone number for delivery contact
     */
    public static Customer createNewCustomer(String authId, String name, String email, String phone) {
        // 1. Create the Value Object first (it validates itself)
        ContactInfo contact = new ContactInfo(email, phone);

        // 2. Return the Entity linked to the Auth system
        return new Customer(authId, name, contact);
    }
}