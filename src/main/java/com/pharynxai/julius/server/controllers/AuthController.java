package com.pharynxai.julius.server.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/signup")
    public UserDTOPayload registerUser(@RequestBody UserDTO user) {
        return userService.saveUsers(user);      
    }
    
    @PostMapping("/signin")
    public ResponseEntity<String> login(@RequestBody UserDTO request) {
        System.err.println(request.email());
        System.err.println(request.password());
        Users user = userService.getUsersByEmail(request.email());
        System.out.println("Matches? " + passwordEncoder.matches(request.password(), user.getPassword()));
        jwtTokenUtil.generateToken("ping");
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
            System.err.println("op " + auth);
            if (auth.isAuthenticated()) {
                return ResponseEntity.ok("Login successful");
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
