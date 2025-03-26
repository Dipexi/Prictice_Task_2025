package com.example.prictice_task_2025.service;

import com.example.prictice_task_2025.security.JWTRequest;
import com.example.prictice_task_2025.security.JWTResponse;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;

public interface AuthService {
    JWTResponse login(@NonNull JWTRequest authRequest) throws AuthException;
    JWTResponse getAccessToken(@NonNull String refreshToken);
    JWTResponse refresh(@NonNull String refreshToken) throws AuthException;

}
