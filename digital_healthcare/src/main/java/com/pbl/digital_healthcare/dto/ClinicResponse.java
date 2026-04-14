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
public class ClinicResponse {
    private Long id;
    private String name;
    private String location;
    private String phone;
    private String email;
    private String hours;
    private String services;
}
