package com.pharynxai.julius.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pharynxai.julius.model.User;

public interface UserRepository extends JpaRepository <User, Long> {}
