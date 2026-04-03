package com.pbl.digital_healthcare.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class AuthResponse {
    private String token;
    private UserResponse user;
}
