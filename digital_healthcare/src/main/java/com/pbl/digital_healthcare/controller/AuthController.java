package com.pbl.digital_healthcare.controller;

import com.pbl.digital_healthcare.dto.AuthResponse;
import com.pbl.digital_healthcare.dto.LoginRequest;
import com.pbl.digital_healthcare.dto.MessageResponse;
import com.pbl.digital_healthcare.dto.RegisterRequest;
import com.pbl.digital_healthcare.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public MessageResponse register(@RequestBody RegisterRequest request){
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request){
        return authService.login(request);
    }
}
