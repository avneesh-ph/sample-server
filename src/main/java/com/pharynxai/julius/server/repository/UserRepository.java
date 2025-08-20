package com.pharynxai.julius.server.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pharynxai.julius.server.model.Users;

public interface UserRepository extends JpaRepository <Users, UUID> {
    Optional<Users> findByEmail(String email);
}
