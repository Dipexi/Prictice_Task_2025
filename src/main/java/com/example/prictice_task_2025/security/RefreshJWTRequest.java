package com.example.prictice_task_2025.security;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class RefreshJWTRequest {

    public String refreshToken;

}