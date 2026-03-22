package com.example.ordermanagement.infrastructure.persistence;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final JpaCustomerRepository jpaRepo;

    public DataInitializer(JpaCustomerRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public void run(String... args) {
        if (!jpaRepo.existsById("user_001")) {
            // Added "AUTH_001" as the second argument
            jpaRepo.save(new CustomerEntity("user_001", "AUTH_001", "Eden Admasu", "eden@hilcoe.edu.et", "0911-000-000"));
            System.out.println("✅ Static User 'user_001' created in Database.");
        }
    }
}