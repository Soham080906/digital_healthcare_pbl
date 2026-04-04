package com.pbl.digital_healthcare.config;

import com.pbl.digital_healthcare.models.Clinic;
import com.pbl.digital_healthcare.models.Doctor;
import com.pbl.digital_healthcare.models.Role;
import com.pbl.digital_healthcare.models.User;
import com.pbl.digital_healthcare.repository.ClinicRepository;
import com.pbl.digital_healthcare.repository.DoctorRepossitory;
import com.pbl.digital_healthcare.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ClinicRepository clinicRepository;
    private final DoctorRepossitory doctorRepossitory;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(ClinicRepository clinicRepository, 
                          DoctorRepossitory doctorRepossitory,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.clinicRepository = clinicRepository;
        this.doctorRepossitory = doctorRepossitory;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== DataInitializer Starting ===");
        System.out.println("Clinic count: " + clinicRepository.count());
        System.out.println("Doctor count: " + doctorRepossitory.count());
        System.out.println("User count: " + userRepository.count());
        
        if (clinicRepository.count() == 0) {
            System.out.println("Initializing sample clinics...");
            initializeClinics();
        } else {
            System.out.println("Clinics already exist, skipping initialization");
        }
        
        if (doctorRepossitory.count() == 0) {
            System.out.println("Initializing sample doctors...");
            initializeDoctors();
        } else {
            System.out.println("Doctors already exist, skipping initialization");
        }
        
        System.out.println("=== DataInitializer Complete ===");
    }

    private void initializeClinics() {
        Clinic clinic1 = Clinic.builder()
                .name("City Medical Center")
                .location("123 Main St, New York, NY")
                .phone("212-555-0123")
                .email("info@citymedical.com")
                .hours("Mon-Fri: 8AM-8PM, Sat: 9AM-5PM")
                .services("Cardiology,Pediatrics,Dermatology,Orthopedics,General Medicine")
                .build();

        Clinic clinic2 = Clinic.builder()
                .name("Westside Health Clinic")
                .location("456 Oak Ave, Los Angeles, CA")
                .phone("310-555-0456")
                .email("contact@westsidehealth.com")
                .hours("Mon-Fri: 7AM-7PM, Sat-Sun: 8AM-4PM")
                .services("Neurology,Psychiatry,General Physician,Dentist,Ophthalmology")
                .build();

        Clinic clinic3 = Clinic.builder()
                .name("Family Care Hospital")
                .location("789 Pine Rd, Chicago, IL")
                .phone("312-555-0789")
                .email("info@familycarehospital.com")
                .hours("24/7 Emergency Services")
                .services("Emergency Care,Gynecology,General Medicine,Pediatrics")
                .build();

        clinicRepository.save(clinic1);
        clinicRepository.save(clinic2);
        clinicRepository.save(clinic3);

        System.out.println("Sample clinics initialized");
    }

    private void initializeDoctors() {
        // Get clinics first
        Clinic clinic1 = clinicRepository.findById(1L).orElse(null);
        Clinic clinic2 = clinicRepository.findById(2L).orElse(null);

        if (clinic1 != null) {
            User user1 = User.builder()
                    .name("Dr. John Smith")
                    .email("john.smith@clinic.com")
                    .password(passwordEncoder.encode("password123"))
                    .role(Role.DOCTOR)
                    .build();
            userRepository.save(user1);

            Doctor doctor1 = Doctor.builder()
                    .user(user1)
                    .specialization("Cardiology")
                    .licenseNumber("MD123456")
                    .experience(15)
                    .education("MD, Harvard Medical School")
                    .phone("212-555-0101")
                    .clinic(clinic1)
                    .build();
            doctorRepossitory.save(doctor1);
        }

        if (clinic2 != null) {
            User user2 = User.builder()
                    .name("Dr. Sarah Johnson")
                    .email("sarah.johnson@clinic.com")
                    .password(passwordEncoder.encode("password123"))
                    .role(Role.DOCTOR)
                    .build();
            userRepository.save(user2);

            Doctor doctor2 = Doctor.builder()
                    .user(user2)
                    .specialization("Pediatrics")
                    .licenseNumber("MD789012")
                    .experience(8)
                    .education("MD, Johns Hopkins University")
                    .phone("310-555-0202")
                    .clinic(clinic2)
                    .build();
            doctorRepossitory.save(doctor2);
        }

        System.out.println("Sample doctors initialized");
    }
}
