package com.pbl.digital_healthcare.controller;

import com.pbl.digital_healthcare.dto.AppointmentRequest;
import com.pbl.digital_healthcare.dto.AppointmentResponse;
import com.pbl.digital_healthcare.dto.BookedSlotsResponse;
import com.pbl.digital_healthcare.dto.MessageResponse;
import com.pbl.digital_healthcare.models.User;
import com.pbl.digital_healthcare.repository.UserRepository;
import com.pbl.digital_healthcare.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final UserRepository userRepository;

    public AppointmentController(AppointmentService appointmentService, UserRepository userRepository) {
        this.appointmentService = appointmentService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> bookAppointment(
            @RequestBody AppointmentRequest request,
            @RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.replace("Bearer ", "");
            String userEmail = extractUserEmailFromToken(token);
            AppointmentResponse response = appointmentService.bookAppointment(request, userEmail);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/booked-slots/{doctorId}/{date}")
    public ResponseEntity<?> getBookedSlots(
            @PathVariable Long doctorId,
            @PathVariable String date) {
        try {
            BookedSlotsResponse response = appointmentService.getBookedSlots(doctorId, date);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/my-appointments")
    public ResponseEntity<?> getUserAppointments(
            @RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.replace("Bearer ", "");
            String userEmail = extractUserEmailFromToken(token);
            List<AppointmentResponse> appointments = appointmentService.getUserAppointments(userEmail);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<?> cancelAppointment(
            @PathVariable Long appointmentId,
            @RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.replace("Bearer ", "");
            String userEmail = extractUserEmailFromToken(token);
            appointmentService.cancelAppointment(appointmentId, userEmail);
            return ResponseEntity.ok(new MessageResponse("Appointment cancelled successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    private String extractUserEmailFromToken(String token) {
        // For development/testing - accept any token and return a real patient user
        // In production, this should validate JWT token and extract actual user email
        if (token != null && !token.isEmpty()) {
            return getExistingPatientUser();
        }
        throw new RuntimeException("Invalid token");
    }
    
    private String getExistingPatientUser() {
        // Find any existing patient user in the database
        // Try to find a user with PATIENT role first
        List<User> patients = userRepository.findAll().stream()
                .filter(user -> user.getRole() == com.pbl.digital_healthcare.models.Role.PATIENT)
                .limit(1)
                .collect(java.util.stream.Collectors.toList());
        
        if (!patients.isEmpty()) {
            return patients.get(0).getEmail();
        }
        
        // If no patient found, try any user
        List<User> allUsers = userRepository.findAll();
        if (!allUsers.isEmpty()) {
            return allUsers.get(0).getEmail();
        }
        
        throw new RuntimeException("No users found in database. Please register a user first using /api/auth/register");
    }
}
