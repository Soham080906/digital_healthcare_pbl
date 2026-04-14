package com.pbl.digital_healthcare.controller;

import com.pbl.digital_healthcare.dto.AppointmentRequest;
import com.pbl.digital_healthcare.dto.AppointmentResponse;
import com.pbl.digital_healthcare.dto.BookedSlotsResponse;
import com.pbl.digital_healthcare.dto.MessageResponse;
import com.pbl.digital_healthcare.models.User;
import com.pbl.digital_healthcare.repository.UserRepository;
import com.pbl.digital_healthcare.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final UserRepository userRepository;

    public AppointmentController(AppointmentService appointmentService, UserRepository userRepository) {
        this.appointmentService = appointmentService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<?> bookAppointment(@RequestBody AppointmentRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();
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
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<?> getUserAppointments() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();
            List<AppointmentResponse> appointments = appointmentService.getUserAppointments(userEmail);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{appointmentId}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long appointmentId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();
            appointmentService.cancelAppointment(appointmentId, userEmail);
            return ResponseEntity.ok(new MessageResponse("Appointment cancelled successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/doctor")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> getDoctorAppointments(
            @RequestParam(required = false) String status) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();
            List<AppointmentResponse> appointments = appointmentService.getDoctorAppointmentsByEmail(userEmail, status);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> getDoctorAppointmentsById(
            @PathVariable Long doctorId,
            @RequestParam(required = false) String status) {
        try {
            List<AppointmentResponse> appointments = appointmentService.getDoctorAppointments(doctorId, status);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/doctor/user/{userId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> getDoctorAppointmentsByUserId(
            @PathVariable Long userId,
            @RequestParam(required = false) String status) {
        try {
            List<AppointmentResponse> appointments = appointmentService.getDoctorAppointmentsByUserId(userId, status);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/{appointmentId}/status")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateAppointmentStatus(
            @PathVariable Long appointmentId,
            @RequestBody Map<String, String> statusUpdate) {
        try {
            String status = statusUpdate.get("status");
            if (status == null || (!status.equals("confirmed") && !status.equals("completed") && !status.equals("cancelled"))) {
                return ResponseEntity.badRequest().body(new MessageResponse("Invalid status. Must be: confirmed, completed, or cancelled"));
            }
            
            AppointmentResponse response = appointmentService.updateAppointmentStatus(appointmentId, status);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{appointmentId}/cancel")
    @PreAuthorize("hasRole('PATIENT') or hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> cancelAppointmentByRole(@PathVariable Long appointmentId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();
            appointmentService.cancelAppointment(appointmentId, userEmail);
            return ResponseEntity.ok(new MessageResponse("Appointment cancelled successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
