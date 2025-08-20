package com.pharynxai.julius.server.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharynxai.julius.server.dto.UserDTO;
import com.pharynxai.julius.server.dto.UserDTOPayload;
import com.pharynxai.julius.server.services.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserDTOPayload registerUser(@RequestBody UserDTO user) {
        return userService.saveUsers(user);      
    }
    
    // @PostMapping("/signin")
    // public Users loginUser(@RequestBody UserDTO user) {
    //     return userService.saveUsers(user);      
    // }

    // @PostMapping("/signout")
    // public Users logoutUser(@RequestBody UserDTO user) {
    //     return userService.saveUsers(user);      
    // }

    // @PostMapping("/oauth/google/callback")
    // public Users googleOauth(@RequestBody UserDTO user) {
    //     return userService.saveUsers(user);      
    // }
}
