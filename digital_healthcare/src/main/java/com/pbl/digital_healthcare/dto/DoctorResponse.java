package com.pbl.digital_healthcare.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DoctorResponse {
    private Long id;
    private UserInfo user;
    private String specialization;
    private String licenseNumber;
    private Integer experience;
    private String education;
    private String phone;
    private ClinicInfo clinic;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserInfo {
        private Long id;
        private String name;
        private String email;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ClinicInfo {
        private Long id;
        private String name;
        private String location;
        private String phone;
        private String email;
        private String hours;
        private String services;
    }
}
