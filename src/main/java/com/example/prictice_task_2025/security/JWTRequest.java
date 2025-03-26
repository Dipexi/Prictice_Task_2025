package com.example.prictice_task_2025.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JWTRequest {
    @NonNull
    private String username;
    @NonNull
    private String password;
}
