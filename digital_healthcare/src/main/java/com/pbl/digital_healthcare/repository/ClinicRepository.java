package com.pbl.digital_healthcare.repository;

import com.pbl.digital_healthcare.models.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
}
