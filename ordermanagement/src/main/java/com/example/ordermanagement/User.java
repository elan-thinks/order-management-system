package com.example.ordermanagement;

import jakarta.persistence.*;

@Entity
@Table(name = "user") // Ensure this matches your MySQL table name exactly
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    // Standard Default Constructor (Required by JPA)
    public User() {}

    // Public Getters (Required for the Browser/JSON)
    public Long getId() { return id; }
    public String getName() { return name; }

    // Setters (Required to save data)
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
}