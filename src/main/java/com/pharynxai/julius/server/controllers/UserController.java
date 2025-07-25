package com.pharynxai.julius.server.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.pharynxai.julius.server.dto.UserDTO;
import com.pharynxai.julius.server.dto.UserDTOPayload;
import com.pharynxai.julius.server.model.Users;
import com.pharynxai.julius.server.services.UserService;

import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping()
    public UserDTOPayload addUser(@RequestBody UserDTO user) {
        return userService.saveUsers(user);
    }

    @GetMapping()
    public List<UserDTOPayload> getAllUsers() {
        return userService.getAllUsers();
    }
    
    @GetMapping("/{id}")
    public UserDTOPayload getUserById(@PathVariable UUID id) {
        return userService.getUsersById(id);
    }
    
    @PutMapping("/{id}")
    public UserDTOPayload putMethodName(@PathVariable UUID id, @RequestBody String entity) {
        
        
        return entity;
    }

    @DeleteMapping("/{id}")
    public UUID deleteUserById(@PathVariable UUID id) {
        userService.deleteUsersById(id);
        return id;
    }
    
}
