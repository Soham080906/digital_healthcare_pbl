package com.pbl.digital_healthcare.service;

import com.pbl.digital_healthcare.dto.*;
import com.pbl.digital_healthcare.models.Clinic;
import com.pbl.digital_healthcare.models.Doctor;
import com.pbl.digital_healthcare.models.Role;
import com.pbl.digital_healthcare.models.User;
import com.pbl.digital_healthcare.repository.ClinicRepository;
import com.pbl.digital_healthcare.repository.DoctorRepossitory;
import com.pbl.digital_healthcare.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DoctorRepossitory doctorRepossitory;
    private final ClinicRepository clinicRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, DoctorRepossitory doctorRepossitory
    , ClinicRepository clinicRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.doctorRepossitory = doctorRepossitory;
        this.clinicRepository = clinicRepository;
    }

    public MessageResponse register(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists");
        }
        Role role = Role.valueOf(request.getRole().toUpperCase());
        User user = User.builder().
                name(request.getName()).
                email(request.getEmail()).
                password(passwordEncoder.encode(request.getPassword())).
                role(role).
                build();
        userRepository.save(user);

        if(role == Role.DOCTOR){
            System.out.println("ClinicId received: " + request.getClinicId());
            Clinic clinic = clinicRepository.findById(request.getClinicId()).orElseThrow(() -> new RuntimeException("Clinic not found"));
            Doctor doctor = Doctor.builder().user(user).
                    specialization(request.getSpecialization()).
                    licenseNumber(request.getLicenseNumber()).
                    experience(request.getExperience()).
                    education(request.getEducation()).
                    phone(request.getPhone()).
                    clinic(clinic).build();
            doctorRepossitory.save(doctor);
        }

        return new MessageResponse("User registered successfully");
    }

    public AuthResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new RuntimeException("User not found"));

        System.out.println("Raw password: " + request.getPassword());
        System.out.println("DB password: " + user.getPassword());
        System.out.println("Matches: " + passwordEncoder.matches(request.getPassword(), user.getPassword()));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid password");
        }
        UserResponse userResponse = UserResponse.builder().
                id(user.getId()).
                email(user.getEmail()).
                name(user.getName()).
                role(user.getRole().name()).
                build();

        return AuthResponse.builder().
                token("Dummy Token").
                user(userResponse).build();

    }

}
