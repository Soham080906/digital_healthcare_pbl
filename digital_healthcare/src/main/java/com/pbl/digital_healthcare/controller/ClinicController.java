package com.pbl.digital_healthcare.controller;

import com.pbl.digital_healthcare.dto.ClinicResponse;
import com.pbl.digital_healthcare.models.Clinic;
import com.pbl.digital_healthcare.models.User;
import com.pbl.digital_healthcare.repository.ClinicRepository;
import com.pbl.digital_healthcare.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clinics")
public class ClinicController {
    private final ClinicRepository clinicRepository;
    private final UserRepository userRepository;

    public ClinicController(ClinicRepository clinicRepository, UserRepository userRepository){
        this.clinicRepository = clinicRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<ClinicResponse> getAllClinics(){
        List<Clinic> clinics = clinicRepository.findAll();
        return clinics.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/manage")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public Map<String, String> getClinicManagementAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Access granted to clinic management");
        response.put("user", email);
        response.put("role", authentication.getAuthorities().toString());
        
        return response;
    }

    private ClinicResponse convertToResponse(Clinic clinic) {
        return ClinicResponse.builder()
                .id(clinic.getId())
                .name(clinic.getName())
                .location(clinic.getLocation())
                .phone(clinic.getPhone())
                .email(clinic.getEmail())
                .hours(clinic.getHours())
                .services(clinic.getServices())
                .build();
    }
}
