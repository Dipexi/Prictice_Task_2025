package com.example.prictice_task_2025.service;

import com.example.prictice_task_2025.entity.User;
import com.example.prictice_task_2025.security.JWTResponse;

public interface RegistrationService {
    JWTResponse register(User user);
}