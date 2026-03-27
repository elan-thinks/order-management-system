package com.example.ordermanagement.infrastructure.persistence;

import com.example.ordermanagement.domain.model.Customer;
import com.example.ordermanagement.domain.repository.CustomerRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JpaCustomerRepository implements CustomerRepository {

    private final SpringDataCustomerRepository jpaRepo;

    public JpaCustomerRepository(SpringDataCustomerRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public void save(Customer customer) {
        // Domain -> Entity
        CustomerEntity entity = CustomerEntity.fromDomain(customer);
        jpaRepo.save(entity);
    }

    @Override
    public Optional<Customer> findByAuthId(String authId) {
        // Entity -> Domain
        return jpaRepo.findByAuthId(authId).map(CustomerEntity::toDomain);
    }

    @Override
    public List<Customer> findAll() {
        return jpaRepo.findAll().stream()
                .map(CustomerEntity::toDomain)
                .collect(Collectors.toList());
    }
    @Override
    public Optional<Customer> findById(Long id) {
        // Map the Entity back to your Domain Model
        return jpaRepo.findById(id).map(CustomerEntity::toDomain);
    }
}