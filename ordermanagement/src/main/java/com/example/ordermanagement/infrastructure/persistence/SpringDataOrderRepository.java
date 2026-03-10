package com.example.ordermanagement.infrastructure.persistence;

import com.example.ordermanagement.infrastructure.persistence.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * This interface handles the actual communication with MySQL.
 * UUID is the type of our ID, and OrderEntity is the table mapping.
 */
// The 'String' here must match the @Id type in OrderEntity
public interface SpringDataOrderRepository extends JpaRepository<OrderEntity, String> {
    List<OrderEntity> findByProductContainingIgnoreCase(String product);

}