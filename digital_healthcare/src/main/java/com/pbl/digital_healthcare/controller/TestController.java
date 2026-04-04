package com.pbl.digital_healthcare.controller;

import com.pbl.digital_healthcare.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    private UserRepository userRepository;

    public TestController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping
    public String test(){
        return "Successfull";
    }

}
