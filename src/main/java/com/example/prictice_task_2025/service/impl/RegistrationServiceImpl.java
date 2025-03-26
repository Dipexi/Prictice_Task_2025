package com.example.prictice_task_2025.service.impl;

import com.example.prictice_task_2025.entity.User;
import com.example.prictice_task_2025.enumeration.Role;
import com.example.prictice_task_2025.repository.UserRepository;
import com.example.prictice_task_2025.security.JWTResponse;
import com.example.prictice_task_2025.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTServiceImpl jwtServiceImpl;

    private User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow();
    }

    @Transactional
    @Override
    public JWTResponse register(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(Role.USER);
        final String accessToken = jwtServiceImpl.generateAccessToken(user);
        final String refreshToken = jwtServiceImpl.generateRefreshToken(user);
        userRepository.save(user);
        return new JWTResponse(accessToken, refreshToken);
    }

}