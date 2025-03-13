package com.example.prictice_task_2025.service;

import com.example.prictice_task_2025.entity.User;

public interface UserService {
    User findByUsername(String username);
}
