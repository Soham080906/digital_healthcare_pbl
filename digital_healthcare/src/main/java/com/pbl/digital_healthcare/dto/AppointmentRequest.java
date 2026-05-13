package com.pbl.digital_healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {
    private Long clinic;
    private Long doctor;
    private String slot;
    private String notes;
}
