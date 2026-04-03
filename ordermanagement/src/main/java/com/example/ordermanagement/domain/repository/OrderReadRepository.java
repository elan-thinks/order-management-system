package com.example.ordermanagement.domain.repository;

import com.example.ordermanagement.domain.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderReadRepository {
    Optional<Order> findByPublicId(UUID publicId);
    List<Order> findAll();
    Optional<Order> findById(Long id);
    long count();

    @Query("SELECT SUM(o.totalPrice) FROM OrderEntity o WHERE o.orderDate >= :startDate GROUP BY o.orderDate ORDER BY o.orderDate ASC")
    List<BigDecimal> getSalesForLast7Days(@Param("startDate") LocalDate startDate);
}
