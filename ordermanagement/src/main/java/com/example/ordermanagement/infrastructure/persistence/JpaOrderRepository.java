package com.example.ordermanagement.infrastructure.persistence;

import com.example.ordermanagement.domain.model.Order;
import com.example.ordermanagement.domain.repository.OrderRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JpaOrderRepository implements OrderRepository {

    private final SpringDataOrderRepository springRepo;

    public JpaOrderRepository(SpringDataOrderRepository springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public void save(Order order) {
        springRepo.save(OrderEntity.fromDomain(order));
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
    public Optional<Order> findById(String id) {
        return springRepo.findById(id).map(OrderEntity::toDomain);
    }

    @Override
    public void deleteById(String id) {
        springRepo.deleteById(id);
    }
}