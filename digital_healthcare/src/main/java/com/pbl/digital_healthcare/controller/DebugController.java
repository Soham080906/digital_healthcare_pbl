package com.pbl.digital_healthcare.controller;

import com.pbl.digital_healthcare.models.User;
import com.pbl.digital_healthcare.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/debug")
public class DebugController {
    
    private final UserRepository userRepository;
    
    public DebugController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        System.out.println("Total users in database: " + users.size());
        users.forEach(user -> System.out.println("User: " + user.getName() + " (" + user.getEmail() + ")"));
        return users;
    }
    
    @GetMapping("/test")
    public String testConnection() {
        try {
            long count = userRepository.count();
            return "Database connection successful! Users count: " + count;
        } catch (Exception e) {
            return "Database connection failed: " + e.getMessage();
        }
    }
}
