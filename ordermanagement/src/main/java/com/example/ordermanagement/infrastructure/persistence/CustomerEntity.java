//package com.example.ordermanagement.infrastructure.persistence;
//
//import com.example.ordermanagement.domain.model.Customer;
//import com.example.ordermanagement.domain.value.ContactInfo;
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Table(name = "customers")
//@Data
//public class CustomerEntity {
//
//    @Id
//    @Column(name = "auth_user_id")
//    private String authUserId;
//
////    @Column(unique = true, nullable = false)
////    private String authId;
//
//    private String fullName;
//    private String email;
//    private String phone;
//
//    // 1. You MUST have a no-args constructor for Hibernate
//    public CustomerEntity() {}
//
//
//    // This constructor MUST take 5 Strings to match your DataInitializer
//    public CustomerEntity(String authUserId, String fullName, String email, String phone) {
//        this.authUserId = authUserId;
////        this.authId = authId;
//        this.fullName = fullName;
//        this.email = email;
//        this.phone = phone;
//    }
//
//    public Customer toDomain() {
//        return new Customer(
//                this.authUserId,
//                this.fullName,
//                new ContactInfo(this.email, this.phone)
//        );
//    }
//}