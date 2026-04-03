package com.example.ordermanagement.domain.repository;

import com.example.ordermanagement.domain.model.Customer;
import java.util.Optional;
import java.util.List;

public interface CustomerRepository {
    Optional<Customer> findById(Long id);

    Optional<Customer> findByAuthId(String authId);
    void save(Customer customer);
    List<Customer> findAll();
}