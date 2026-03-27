package com.example.ordermanagement.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Use INTERFACE, not class. Use CustomerEntity, not Customer.
public interface SpringDataCustomerRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByAuthId(String authId);
}