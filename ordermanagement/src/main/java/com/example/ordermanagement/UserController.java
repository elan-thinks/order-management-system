package com.example.ordermanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<com.example.ordermanagement.User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/add-user")
    public String addUser(@RequestParam String name) {
        com.example.ordermanagement.User newUser = new com.example.ordermanagement.User();
        newUser.setName(name);
        userRepository.save(newUser);
        return "Successfully saved " + name + " to the database!";
    }

    @PostMapping("/users")
    public com.example.ordermanagement.User createUser(@RequestBody com.example.ordermanagement.User user) {
        return userRepository.save(user);
    }
}
