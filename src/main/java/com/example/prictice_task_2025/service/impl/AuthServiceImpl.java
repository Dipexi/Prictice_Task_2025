package com.example.prictice_task_2025.service.impl;

import com.example.prictice_task_2025.entity.User;
import com.example.prictice_task_2025.security.JWTRequest;
import com.example.prictice_task_2025.security.JWTResponse;
import com.example.prictice_task_2025.service.AuthService;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserServiceImpl userServiceImpl;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JWTServiceImpl jwtServiceImpl;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JWTResponse login(@NonNull JWTRequest authRequest) throws AuthException {
        final User user = userServiceImpl.findByUsername(authRequest.getUsername());
        if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            final String accessToken = jwtServiceImpl.generateAccessToken(user);
            final String refreshToken = jwtServiceImpl.generateRefreshToken(user);
            refreshStorage.put(user.getUsername(), refreshToken);
            return new JWTResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    @Override
    public JWTResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtServiceImpl.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtServiceImpl.getRefreshClaims(refreshToken);
            final String username = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(username);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userServiceImpl.findByUsername(username);
                final String accessToken = jwtServiceImpl.generateAccessToken(user);
                return new JWTResponse(accessToken, null);
            }
        }
        return new JWTResponse(null, null);
    }

    @Override
    public JWTResponse refresh(@NonNull String refreshToken) throws AuthException {
        if (jwtServiceImpl.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtServiceImpl.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userServiceImpl.findByUsername(login);
                final String accessToken = jwtServiceImpl.generateAccessToken(user);
                final String newRefreshToken = jwtServiceImpl.generateRefreshToken(user);
                refreshStorage.put(user.getUsername(), newRefreshToken);
                return new JWTResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }

}