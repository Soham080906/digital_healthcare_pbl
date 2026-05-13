package com.pbl.digital_healthcare.repository;

import com.pbl.digital_healthcare.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepossitory extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserId(Long userId);
}
