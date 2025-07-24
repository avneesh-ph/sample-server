package com.pharynxai.julius.server.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharynxai.julius.server.dto.SignupDTO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @PostMapping("/signup")
    public SignupDTO registerUser(@RequestBody SignupDTO entity) {
        
        
        
        return entity;
    }
    
}
