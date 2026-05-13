package com.pbl.digital_healthcare.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/specializations")
@CrossOrigin
public class SpecializationController {

    @GetMapping
    public List<String> getSpecializations() {
        return List.of(
                "Cardiologist",
                "Pediatrician",
                "Dermatologist",
                "Orthopedic Surgeon",
                "Neurologist",
                "Psychiatrist",
                "General Physician",
                "Dentist",
                "Ophthalmologist",
                "Gynecologist",
                "Sample backend"
        );
    }
}
