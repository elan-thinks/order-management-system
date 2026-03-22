package com.example.ordermanagement.domain.repository;

import com.example.ordermanagement.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // This allows you to find the customer by their "Eden Admasu" / "user_001" ID
    Optional<Customer> findByAuthId(String authId);
}