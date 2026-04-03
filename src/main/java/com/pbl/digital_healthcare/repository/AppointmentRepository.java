package com.pbl.digital_healthcare.repository;

import com.pbl.digital_healthcare.models.Appointment;
import com.pbl.digital_healthcare.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorAndSlotBetween(
            Doctor doctor,
            LocalDateTime start,
            LocalDateTime end
    );

    boolean existsByDoctorAndSlot(Doctor doctor, LocalDateTime slot);
}
