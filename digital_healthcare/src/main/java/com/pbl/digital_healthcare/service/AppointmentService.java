package com.pbl.digital_healthcare.service;

import com.pbl.digital_healthcare.dto.AppointmentRequest;
import com.pbl.digital_healthcare.dto.AppointmentResponse;
import com.pbl.digital_healthcare.dto.BookedSlotsResponse;
import com.pbl.digital_healthcare.models.*;
import com.pbl.digital_healthcare.repository.AppointmentRepository;
import com.pbl.digital_healthcare.repository.ClinicRepository;
import com.pbl.digital_healthcare.repository.DoctorRepossitory;
import com.pbl.digital_healthcare.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorRepossitory doctorRepository;
    private final ClinicRepository clinicRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                           UserRepository userRepository,
                           DoctorRepossitory doctorRepository,
                           ClinicRepository clinicRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.clinicRepository = clinicRepository;
    }

    public AppointmentResponse bookAppointment(AppointmentRequest request, String userEmail) {
        // Validate request
        if (request.getClinic() == null || request.getDoctor() == null || request.getSlot() == null) {
            throw new IllegalArgumentException("Clinic, doctor, and slot are required");
        }

        User patient = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Doctor doctor = doctorRepository.findById(request.getDoctor())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Clinic clinic = clinicRepository.findById(request.getClinic())
                .orElseThrow(() -> new RuntimeException("Clinic not found"));

        LocalDateTime slot = null;
        try {
            // Try multiple datetime formats
            String slotStr = request.getSlot();
            System.out.println("DEBUG: Received slot format: " + slotStr);
            
            if (slotStr.contains("T") && slotStr.contains(":")) {
                if (slotStr.endsWith("Z")) {
                    // ISO format with UTC timezone: 2024-12-25T10:00:00.000Z
                    if (slotStr.contains(".")) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        slot = LocalDateTime.parse(slotStr, formatter);
                    } else {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        slot = LocalDateTime.parse(slotStr, formatter);
                    }
                } else if (slotStr.contains(".")) {
                    // ISO format with milliseconds: 2024-12-25T10:00:00.000
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    slot = LocalDateTime.parse(slotStr, formatter);
                } else if (slotStr.split("T")[1].split(":").length == 3) {
                    // ISO format with seconds: 2024-12-25T10:00:00
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                    slot = LocalDateTime.parse(slotStr, formatter);
                } else {
                    // ISO format without seconds: 2024-12-25T10:00
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                    slot = LocalDateTime.parse(slotStr, formatter);
                }
            } else if (slotStr.contains(" ")) {
                // Format: 2024-12-25 10:00:00
                String[] parts = slotStr.split(" ");
                if (parts.length == 2) {
                    String[] timeParts = parts[1].split(":");
                    if (timeParts.length == 3) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        slot = LocalDateTime.parse(slotStr, formatter);
                    } else if (timeParts.length == 2) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        slot = LocalDateTime.parse(slotStr, formatter);
                    }
                } else {
                    throw new RuntimeException("Invalid space-separated datetime format");
                }
            } else {
                // Try ISO as fallback
                slot = LocalDateTime.parse(slotStr);
            }
        } catch (Exception e) {
            System.out.println("DEBUG: Parse error: " + e.getMessage());
            throw new RuntimeException("Invalid slot format: '" + request.getSlot() + "'. Expected formats: yyyy-MM-ddTHH:mm:ss, yyyy-MM-dd HH:mm:ss, yyyy-MM-ddTHH:mm, or ISO format with optional timezone");
        }
        
        if (slot == null) {
            throw new RuntimeException("Failed to parse slot datetime");
        }

        if (slot.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot book appointments in the past");
        }

        if (appointmentRepository.existsByDoctorAndSlot(doctor, slot)) {
            throw new RuntimeException("Time slot already booked");
        }

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .clinic(clinic)
                .slot(slot)
                .status(AppointmentStatus.CONFIRMED)
                .notes(request.getNotes())
                .build();

        appointment = appointmentRepository.save(appointment);
        return convertToResponse(appointment);
    }

    public BookedSlotsResponse getBookedSlots(Long doctorId, String date) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);

        System.out.println("DEBUG: Searching for appointments between " + startOfDay + " and " + endOfDay);
        
        List<Appointment> appointments = appointmentRepository.findByDoctorAndSlotBetweenAndStatus(
                doctor, startOfDay, endOfDay, AppointmentStatus.CONFIRMED);

        System.out.println("DEBUG: Found " + appointments.size() + " confirmed appointments for doctor " + doctorId + " on " + date);
        
        // Also check all appointments (including cancelled) for debugging
        List<Appointment> allAppointments = appointmentRepository.findByDoctorAndSlotBetween(
                doctor, startOfDay, endOfDay);
        System.out.println("DEBUG: Total appointments (all statuses): " + allAppointments.size());
        allAppointments.forEach(apt -> 
            System.out.println("DEBUG: Appointment at " + apt.getSlot() + " with status " + apt.getStatus()));

        List<String> bookedSlots = appointments.stream()
                .map(appointment -> appointment.getSlot().format(DateTimeFormatter.ofPattern("HH:mm")))
                .collect(Collectors.toList());

        System.out.println("DEBUG: Booked slots for doctor " + doctorId + " on " + date + ": " + bookedSlots);

        return BookedSlotsResponse.builder()
                .bookedSlots(bookedSlots)
                .build();
    }

    public List<AppointmentResponse> getUserAppointments(String userEmail) {
        User patient = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("DEBUG: Fetching appointments for patient - Email: " + userEmail + ", ID: " + patient.getId());
        
        List<Appointment> appointments = appointmentRepository.findByPatientId(patient.getId());
        
        System.out.println("DEBUG: Found " + appointments.size() + " appointments for patient ID " + patient.getId());

        return appointments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public void cancelAppointment(Long appointmentId, String userEmail) {
        User patient = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        System.out.println("DEBUG: Cancelling appointment " + appointmentId + " with current status: " + appointment.getStatus());

        if (!appointment.getPatient().getId().equals(patient.getId())) {
            throw new RuntimeException("You can only cancel your own appointments");
        }

        if (appointment.getSlot().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot cancel past appointments");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment = appointmentRepository.save(appointment);
        System.out.println("DEBUG: Appointment " + appointmentId + " status updated to: " + appointment.getStatus());
    }

    private AppointmentResponse convertToResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patient(AppointmentResponse.PatientInfo.builder()
                        .id(appointment.getPatient().getId())
                        .name(appointment.getPatient().getName())
                        .email(appointment.getPatient().getEmail())
                        .build())
                .doctor(AppointmentResponse.DoctorInfo.builder()
                        .id(appointment.getDoctor().getId())
                        .name(appointment.getDoctor().getUser().getName())
                        .specialization(appointment.getDoctor().getSpecialization())
                        .build())
                .clinic(AppointmentResponse.ClinicInfo.builder()
                        .id(appointment.getClinic().getId())
                        .name(appointment.getClinic().getName())
                        .build())
                .slot(appointment.getSlot())
                .status(appointment.getStatus().name())
                .notes(appointment.getNotes())
                .build();
    }

    public List<AppointmentResponse> getDoctorAppointments(Long doctorId, String status) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<Appointment> appointments;
        if (status != null && !status.equals("all")) {
            if (status.equals("active")) {
                // Active appointments include CONFIRMED and PENDING
                List<Appointment> confirmedAppointments = appointmentRepository.findByDoctorAndStatus(doctor, AppointmentStatus.CONFIRMED);
                List<Appointment> pendingAppointments = appointmentRepository.findByDoctorAndStatus(doctor, AppointmentStatus.PENDING);
                appointments = new java.util.ArrayList<>(confirmedAppointments);
                appointments.addAll(pendingAppointments);
            } else {
                try {
                    AppointmentStatus appointmentStatus = AppointmentStatus.valueOf(status.toUpperCase());
                    appointments = appointmentRepository.findByDoctorAndStatus(doctor, appointmentStatus);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid status. Must be: all, active, confirmed, cancelled, or pending");
                }
            }
        } else {
            appointments = appointmentRepository.findByDoctor(doctor);
        }

        return appointments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getDoctorAppointmentsByUserId(Long userId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Doctor doctor = doctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));

        List<Appointment> appointments;
        if (status != null && !status.equals("all")) {
            if (status.equals("active")) {
                // Active appointments include CONFIRMED and PENDING
                List<Appointment> confirmedAppointments = appointmentRepository.findByDoctorAndStatus(doctor, AppointmentStatus.CONFIRMED);
                List<Appointment> pendingAppointments = appointmentRepository.findByDoctorAndStatus(doctor, AppointmentStatus.PENDING);
                appointments = new java.util.ArrayList<>(confirmedAppointments);
                appointments.addAll(pendingAppointments);
            } else {
                try {
                    AppointmentStatus appointmentStatus = AppointmentStatus.valueOf(status.toUpperCase());
                    appointments = appointmentRepository.findByDoctorAndStatus(doctor, appointmentStatus);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid status. Must be: all, active, confirmed, cancelled, or pending");
                }
            }
        } else {
            appointments = appointmentRepository.findByDoctor(doctor);
        }

        return appointments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getDoctorAppointmentsByEmail(String userEmail, String status) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Doctor doctor = doctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));

        List<Appointment> appointments;
        if (status != null && !status.equals("all")) {
            try {
                AppointmentStatus appointmentStatus = AppointmentStatus.valueOf(status.toUpperCase());
                appointments = appointmentRepository.findByDoctorAndStatus(doctor, appointmentStatus);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid status. Must be: all, confirmed, completed, or cancelled");
            }
        } else {
            appointments = appointmentRepository.findByDoctor(doctor);
        }

        return appointments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public AppointmentResponse updateAppointmentStatus(Long appointmentId, String status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        try {
            AppointmentStatus newStatus = AppointmentStatus.valueOf(status.toUpperCase());
            appointment.setStatus(newStatus);
            appointment.setUpdatedAt(LocalDateTime.now());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status. Must be: confirmed, completed, or cancelled");
        }

        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return convertToResponse(updatedAppointment);
    }
}
