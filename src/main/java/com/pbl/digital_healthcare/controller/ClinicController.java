package com.pbl.digital_healthcare.controller;

import com.pbl.digital_healthcare.models.Clinic;
import com.pbl.digital_healthcare.repository.ClinicRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clinics")
@CrossOrigin
public class ClinicController {
    private final ClinicRepository clinicRepository;

    public ClinicController(ClinicRepository clinicRepository){
        this.clinicRepository = clinicRepository;
    }

    @GetMapping
    public List<Clinic> getAllClinics(){
        return clinicRepository.findAll();
    }
}
