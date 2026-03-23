package com.example.ordermanagement.domain.repository;

import com.example.ordermanagement.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Add this line:
    Optional<Customer> findByAuthId(String authId);
}