package com.example.ordermanagement.infrastructure.persistence;

import com.example.ordermanagement.domain.model.Customer;
import com.example.ordermanagement.domain.value.ContactInfo;
import com.example.ordermanagement.domain.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepo;

    public CustomerRepository getCustomerRepo() {
        return customerRepo;
    }

    public DataInitializer(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    public void run(String... args) {
        if (customerRepo.findByAuthId("user_001").isEmpty()) {

            ContactInfo contact = new ContactInfo("ema@hilcoe.edu.et", "0911-000-000");

            // Updated: Passing 'null' for the ID because the Database will generate it
            Customer newCustomer = new Customer(
                    null,
                    "user_001",
                    "Ema",
                    contact
            );

            customerRepo.save(newCustomer);
            System.out.println("✅ Static User 'user_001' created in Database.");
        } else {
            System.out.println("ℹ️ User 'user_001' already exists. Skipping initialization.");
        }
    }
}