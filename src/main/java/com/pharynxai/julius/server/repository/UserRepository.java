package com.pharynxai.julius.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pharynxai.julius.server.model.User;

public interface UserRepository extends JpaRepository <User, Long> {}
