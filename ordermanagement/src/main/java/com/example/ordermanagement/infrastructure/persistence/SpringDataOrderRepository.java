//package com.example.ordermanagement.infrastructure.persistence;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import java.util.List;
//
//public interface SpringDataOrderRepository
//        extends JpaRepository<OrderEntity, String> {
//
//    @Query("""
//        SELECT DISTINCT o
//        FROM OrderEntity o
//        JOIN o.items i
//        WHERE LOWER(i.product) LIKE LOWER(CONCAT('%', :product, '%'))
//    """)
//    List<OrderEntity> searchByProduct(String product);
//}

package com.example.ordermanagement.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataOrderRepository extends JpaRepository<OrderEntity, String> {
}