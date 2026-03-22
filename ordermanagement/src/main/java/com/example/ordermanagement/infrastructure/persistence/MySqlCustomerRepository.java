//package com.example.ordermanagement.infrastructure.persistence;
//
//import com.example.ordermanagement.domain.model.Customer;
//import com.example.ordermanagement.domain.repository.CustomerRepository;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Component
//public class MySqlCustomerRepository implements CustomerRepository { // ✅ ONLY your domain interface
//
//    private final JpaCustomerRepository jpaRepository; // This 'injects' the magic interface
//
//    public MySqlCustomerRepository(JpaCustomerRepository jpaRepository) {
//        this.jpaRepository = jpaRepository;
//    }
//
//    @Override
//    public Optional<Customer> findByAuthId(String authId) {
//        // We use the JPA interface to find by ID ("user_001")
//        return jpaRepository.findById(authId)
//                .map(CustomerEntity::toDomain);
//    }
//
//    @Override
//    public void save(Customer customer) {
//        jpaRepository.save(CustomerEntity.fromDomain(customer));
//    }
//
//    @Override
//    public void deleteById(String id) {
//        jpaRepository.deleteById(id);
//    }
//}