package com.example.ordermanagement.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataOrderRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByPublicId(UUID publicId);

    @Query("SELECT SUM(o.amount) FROM OrderEntity o " + // Changed totalPrice -> amount
            "WHERE o.createdAt >= :startDate " +
            "GROUP BY o.createdAt " +
            "ORDER BY o.createdAt ASC")
    List<BigDecimal> getSalesForLast7Days(@Param("startDate") java.time.LocalDate startDate); // Changed to LocalDate
}