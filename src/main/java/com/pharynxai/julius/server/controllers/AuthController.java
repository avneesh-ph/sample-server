package com.pharynxai.julius.server.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharynxai.julius.server.dto.JwtResponse;
import com.pharynxai.julius.server.dto.UserDTO;
import com.pharynxai.julius.server.dto.UserDTOPayload;
import com.pharynxai.julius.server.model.Users;
import com.pharynxai.julius.server.security.JwtTokenUtil;
import com.pharynxai.julius.server.services.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenUtil jwtTokenUtil;

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/signup")
    public UserDTOPayload registerUser(@RequestBody UserDTO user) {
        return userService.saveUsers(user);      
    }
    
    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody UserDTO request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
            if (auth.isAuthenticated()) {
                String token = jwtTokenUtil.generateToken(request.email());
                return ResponseEntity.ok(new JwtResponse(token));
                // return ResponseEntity.ok("Login successful");
            }
        } catch (Exception e) {
            System.err.println(e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // @PostMapping("/signout")
    // public Users logoutUser(@RequestBody UserDTO user) {
    //     return userService.saveUsers(user);      
    // }

    // @PostMapping("/oauth/google/callback")
    // public Users googleOauth(@RequestBody UserDTO user) {
    //     return userService.saveUsers(user);      
    // }
}
