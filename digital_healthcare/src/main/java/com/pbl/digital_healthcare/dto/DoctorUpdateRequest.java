package com.pbl.digital_healthcare.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorUpdateRequest {
    private String name;
    private String email;
    private String specialization;
    private String licenseNumber;
    private Integer experience;
    private String education;
    private String phone;
}
