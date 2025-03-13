package com.example.prictice_task_2025.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Отключаем CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/registration").permitAll() // Разрешаем доступ к регистрации без аутентификации
                        .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
                )
                .httpBasic(basic -> {}); // Используем базовую аутентификацию

        return http.build();
    }
}