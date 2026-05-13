package com.pbl.digital_healthcare.controller;

import com.pbl.digital_healthcare.dto.DoctorResponse;
import com.pbl.digital_healthcare.dto.DoctorUpdateRequest;
import com.pbl.digital_healthcare.models.Doctor;
import com.pbl.digital_healthcare.models.User;
import com.pbl.digital_healthcare.repository.DoctorRepossitory;
import com.pbl.digital_healthcare.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    private final DoctorRepossitory doctorRepossitory;
    private final UserRepository userRepository;

    public DoctorController(DoctorRepossitory doctorRepossitory, UserRepository userRepository) {
        this.doctorRepossitory = doctorRepossitory;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<DoctorResponse> getAllDoctors() {
        List<Doctor> doctors = doctorRepossitory.findAll();
        return doctors.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('DOCTOR')")
    public DoctorResponse getDoctorProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Doctor doctor = doctorRepossitory.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));
        
        return convertToResponse(doctor);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public DoctorResponse getDoctorByUserId(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Doctor doctor = doctorRepossitory.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));
        
        return convertToResponse(doctor);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public DoctorResponse updateDoctorProfile(@PathVariable Long userId, @RequestBody DoctorUpdateRequest updateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Doctor doctor = doctorRepossitory.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));
        
        // Update user information
        if (updateRequest.getName() != null) {
            user.setName(updateRequest.getName());
        }
        if (updateRequest.getEmail() != null) {
            user.setEmail(updateRequest.getEmail());
        }
        
        // Update doctor information
        if (updateRequest.getSpecialization() != null) {
            doctor.setSpecialization(updateRequest.getSpecialization());
        }
        if (updateRequest.getLicenseNumber() != null) {
            doctor.setLicenseNumber(updateRequest.getLicenseNumber());
        }
        if (updateRequest.getExperience() != null) {
            doctor.setExperience(updateRequest.getExperience());
        }
        if (updateRequest.getEducation() != null) {
            doctor.setEducation(updateRequest.getEducation());
        }
        if (updateRequest.getPhone() != null) {
            doctor.setPhone(updateRequest.getPhone());
        }
        
        userRepository.save(user);
        doctorRepossitory.save(doctor);
        
        return convertToResponse(doctor);
    }

    private DoctorResponse convertToResponse(Doctor doctor) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .user(DoctorResponse.UserInfo.builder()
                        .id(doctor.getUser().getId())
                        .name(doctor.getUser().getName())
                        .email(doctor.getUser().getEmail())
                        .build())
                .specialization(doctor.getSpecialization())
                .licenseNumber(doctor.getLicenseNumber())
                .experience(doctor.getExperience())
                .education(doctor.getEducation())
                .phone(doctor.getPhone())
                .clinic(doctor.getClinic() != null ? 
                    DoctorResponse.ClinicInfo.builder()
                        .id(doctor.getClinic().getId())
                        .name(doctor.getClinic().getName())
                        .location(doctor.getClinic().getLocation())
                        .phone(doctor.getClinic().getPhone())
                        .email(doctor.getClinic().getEmail())
                        .hours(doctor.getClinic().getHours())
                        .services(doctor.getClinic().getServices())
                        .build() : null)
                .build();
    }
}
