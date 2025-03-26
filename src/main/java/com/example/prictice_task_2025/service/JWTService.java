package com.example.prictice_task_2025.service;

import com.example.prictice_task_2025.entity.User;
import io.jsonwebtoken.Claims;
import lombok.NonNull;

import java.security.Key;

public interface JWTService {
    String generateAccessToken(@NonNull User user);
    String generateRefreshToken(@NonNull User user);
    boolean validateAccessToken(@NonNull String accessToken);
    boolean validateRefreshToken(@NonNull String refreshToken);
    boolean validateToken(@NonNull String token, @NonNull Key secret);
    Claims getAccessClaims(@NonNull String token);
    Claims getRefreshClaims(@NonNull String token);
    Claims getClaims(@NonNull String token, @NonNull Key secret);
}
