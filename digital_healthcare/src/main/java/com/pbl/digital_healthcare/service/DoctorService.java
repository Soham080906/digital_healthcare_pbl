package com.pbl.digital_healthcare.service;

import com.pbl.digital_healthcare.models.Doctor;
import com.pbl.digital_healthcare.repository.DoctorRepossitory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepossitory doctorRepossitory;

    public DoctorService(DoctorRepossitory doctorRepossitory) {
        this.doctorRepossitory = doctorRepossitory;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepossitory.findAll();
    }
}
