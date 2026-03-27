package com.example.ordermanagement.domain.factory;

import com.example.ordermanagement.domain.model.Customer;
import com.example.ordermanagement.domain.value.ContactInfo;

public class CustomerFactory {

    public static Customer createNewCustomer(String authId, String name, String email, String phone) {
        // 1. Create the Value Object
        ContactInfo contact = new ContactInfo(email, phone);

        // 2. Return the Domain Model with a NULL id (MySQL will generate the real ID on save)
        return new Customer(null, authId, name, contact);
    }
}