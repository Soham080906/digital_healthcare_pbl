package com.pbl.digital_healthcare.controller;

import com.pbl.digital_healthcare.models.Doctor;
import com.pbl.digital_healthcare.repository.DoctorRepossitory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin
public class DoctorController {
    private final DoctorRepossitory doctorRepossitory;

    public DoctorController(DoctorRepossitory doctorRepossitory) {
        this.doctorRepossitory = doctorRepossitory;
    }

    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorRepossitory.findAll();
    }
}
