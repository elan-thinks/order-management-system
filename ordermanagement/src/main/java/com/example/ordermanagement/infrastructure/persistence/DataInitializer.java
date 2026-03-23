package com.example.ordermanagement.infrastructure.persistence;

import com.example.ordermanagement.domain.model.Customer;
import com.example.ordermanagement.domain.value.ContactInfo;
import com.example.ordermanagement.domain.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    // Use your Domain Repository interface here
    private final CustomerRepository customerRepo;

    public DataInitializer(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    public void run(String... args) {
        // 1. Search by the String 'authId', NOT the Long 'id'
        if (customerRepo.findByAuthId("user_001").isEmpty()) {

            // 2. Create the Value Object for contact info
            ContactInfo contact = new ContactInfo("eden@hilcoe.edu.et", "0911-000-000");

            // 3. Create the Customer (Notice: No 'Long id' passed here!)
            Customer newCustomer = new Customer(
                    "user_001",
                    "Eden Admasu",
                    contact
            );

            customerRepo.save(newCustomer);
            System.out.println("✅ Static User 'user_001' created in Database.");
        } else {
            System.out.println("ℹ️ User 'user_001' already exists. Skipping initialization.");
        }
    }
}