package com.pharynxai.julius.server.dto;

import java.util.UUID;

public record UserDTO(UUID id, String email, String password) {}
