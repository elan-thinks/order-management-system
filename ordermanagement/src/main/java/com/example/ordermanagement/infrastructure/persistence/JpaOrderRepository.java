package com.example.ordermanagement.infrastructure.persistence;

import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.repository.OrderReadRepository;
import com.example.ordermanagement.domain.repository.OrderWriteRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JpaOrderRepository implements OrderReadRepository, OrderWriteRepository {

    private final SpringDataOrderRepository springRepo;

    public JpaOrderRepository(SpringDataOrderRepository springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public void save(Order order) {
        OrderEntity entity = OrderEntity.fromDomain(order);
        springRepo.save(entity);
    }

    @Override
    public List<Order> findAll() {
        return springRepo.findAll().stream()
                .map(OrderEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return springRepo.count();
    }

    @Override
    public Optional<Order> findById(Long id) { // Changed to Long
        return springRepo.findById(id).map(OrderEntity::toDomain);
    }
    @Override
    public Optional<Order> findByPublicId(java.util.UUID publicId) {
        return springRepo.findByPublicId(publicId).map(OrderEntity::toDomain);
    }

    @Override
    public void deleteById(Long id) { // Changed to Long
        springRepo.deleteById(id);
    }

    // Inside JpaOrderRepository.java
    @Override
    public List<BigDecimal> getSalesForLast7Days(java.time.LocalDate startDate) {
        return springRepo.getSalesForLast7Days(startDate);
    }
}