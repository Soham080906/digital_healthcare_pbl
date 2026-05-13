package com.pbl.digital_healthcare.controller;

import com.pbl.digital_healthcare.models.User;
import com.pbl.digital_healthcare.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private final UserRepository userRepository;

    public PatientController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('PATIENT')")
    public Map<String, Object> getPatientProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());
        
        return response;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('PATIENT')")
    public Map<String, String> getPatientDashboard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to your patient dashboard");
        response.put("email", email);
        response.put("status", "active");
        
        return response;
    }
}
