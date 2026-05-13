package com.pbl.digital_healthcare.dto;

import com.pbl.digital_healthcare.models.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String role;

    private String specialization;
    private String licenseNumber;
    private Integer experience;
    private String education;
    private Long clinicId;
    private String phone;
}
