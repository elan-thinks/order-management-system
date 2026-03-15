package com.example.ordermanagement.infrastructure.persistence;

import com.example.ordermanagement.domain.model.Order;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
//    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "order_date") // Matches UI column 1
    private LocalDate orderDate;

    @Column(name = "payment")    // Matches UI column 7
    private String payment;

    @Column(name = "status")     // Matches UI column 8
    private String status;
//    private String id;
//    private String status;
//    private String payment;
//    private LocalDate orderDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItemEntity> items;

    public OrderEntity() {}

    public static OrderEntity fromDomain(Order order) {
        OrderEntity entity = new OrderEntity();

        entity.id = order.getId();
        entity.status = order.getStatus();
        entity.payment = order.getPayment();
        entity.orderDate = order.getDate();
        entity.items = order.getItems().stream()
                .map(OrderItemEntity::fromDomain)
                .collect(Collectors.toList());
        return entity;
    }

    public Order toDomain() {
        Order order = new Order(id, status, payment, orderDate);
        if (items != null) {
            items.forEach(item -> order.addItem(item.toDomain()));
        }
        return order;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPayment() { return payment; }
    public void setPayment(String payment) { this.payment = payment; }
    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
    public List<OrderItemEntity> getItems() { return items; }
    public void setItems(List<OrderItemEntity> items) { this.items = items; }
}