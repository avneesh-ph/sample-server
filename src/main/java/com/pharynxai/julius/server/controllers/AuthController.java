package com.pharynxai.julius.server.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharynxai.julius.server.dto.UserDTO;
import com.pharynxai.julius.server.dto.UserDTOPayload;
import com.pharynxai.julius.server.security.JwtTokenUtil;
import com.pharynxai.julius.server.services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<?> login(@RequestBody UserDTO request, HttpServletResponse response) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
            if (auth.isAuthenticated()) {
                String token = jwtTokenUtil.generateToken(request.email());
                jwtTokenUtil.addJwtToCookie(response, token);
                // return ResponseEntity.ok(new JwtResponse(token));
                return ResponseEntity.ok("Login successful");
            }
        } catch (Exception e) {
            System.err.println(e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/signout")
    public ResponseEntity<String> logoutUser(HttpServletResponse response) {
        Cookie cookie = new Cookie("access-token", null);
        cookie.setHttpOnly(false);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out successfully");
    }

    // @PostMapping("/oauth/google/callback")
    // public Users googleOauth(@RequestBody UserDTO user) {
    //     return userService.saveUsers(user);      
    // }
}
