package com.example.prictice_task_2025.security;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JWTResponse {
    private String accessToken;
    private String refreshToken;

}
