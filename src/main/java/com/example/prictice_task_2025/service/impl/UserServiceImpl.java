package com.example.prictice_task_2025.service.impl;

import com.example.prictice_task_2025.entity.User;
import com.example.prictice_task_2025.repository.UserRepository;
import com.example.prictice_task_2025.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username '" + username + "' not found"));
    }

}