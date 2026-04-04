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

        List<Appointment> appointments = appointmentRepository.findByDoctorAndSlotBetweenAndStatus(
                doctor, startOfDay, endOfDay, AppointmentStatus.CONFIRMED);

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

        List<Appointment> appointments = appointmentRepository.findByPatient(patient);

        return appointments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public void cancelAppointment(Long appointmentId, String userEmail) {
        User patient = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (!appointment.getPatient().getId().equals(patient.getId())) {
            throw new RuntimeException("You can only cancel your own appointments");
        }

        if (appointment.getSlot().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot cancel past appointments");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
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
}
