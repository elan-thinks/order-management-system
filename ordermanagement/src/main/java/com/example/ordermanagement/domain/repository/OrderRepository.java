//package com.example.ordermanagement.domain.repository;
//
//import com.example.ordermanagement.domain.model.Order;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//public interface OrderRepository {
//    void save(Order order);
//    List<Order> findAll();
//    long count();
//    Optional<Order> findByPublicId(UUID publicId);
//
//    Optional<Order> findById(Long id);
//    void deleteById(Long id);
//
//    @Query("SELECT SUM(o.totalPrice) FROM OrderEntity o WHERE o.orderDate >= :startDate GROUP BY o.orderDate ORDER BY o.orderDate ASC")
//    List<BigDecimal> getSalesForLast7Days(@Param("startDate") LocalDate startDate);
//}