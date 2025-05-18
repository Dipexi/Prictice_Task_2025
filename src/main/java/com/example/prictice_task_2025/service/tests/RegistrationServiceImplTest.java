package com.example.prictice_task_2025.service.tests;

import com.example.prictice_task_2025.entity.User;
import com.example.prictice_task_2025.enumeration.Role;
import com.example.prictice_task_2025.repository.UserRepository;
import com.example.prictice_task_2025.security.JWTResponse;
import com.example.prictice_task_2025.service.impl.RegistrationServiceImpl;
import com.example.prictice_task_2025.service.impl.JWTServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationServiceImplTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JWTServiceImpl jwtServiceImpl;
    private RegistrationServiceImpl registrationService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtServiceImpl = mock(JWTServiceImpl.class);

        registrationService = new RegistrationServiceImpl(userRepository, passwordEncoder, jwtServiceImpl);
    }

    @Test
    void register_ShouldEncodePassword_SaveUser_AndReturnTokens() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("plainPassword");
        user.setRole(null);

        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode("plainPassword")).thenReturn(encodedPassword);

        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        when(jwtServiceImpl.generateAccessToken(any(User.class))).thenReturn(accessToken);
        when(jwtServiceImpl.generateRefreshToken(any(User.class))).thenReturn(refreshToken);

        JWTResponse jwtResponse = registrationService.register(user);

        assertEquals(encodedPassword, user.getPassword());
        assertEquals(Role.USER, user.getRole());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals(encodedPassword, savedUser.getPassword());
        assertEquals(Role.USER, savedUser.getRole());

        assertEquals(accessToken, jwtResponse.getAccessToken());
        assertEquals(refreshToken, jwtResponse.getRefreshToken());
    }

    @Test
    void register_ShouldPreserveRoleIfAlreadySet() {
        User user = new User();
        user.setUsername("adminUser");
        user.setPassword("adminPassword");
        user.setRole(Role.ADMIN);

        String encodedPassword = "encodedAdminPassword";
        when(passwordEncoder.encode("adminPassword")).thenReturn(encodedPassword);

        String accessToken = "admin-access-token";
        String refreshToken = "admin-refresh-token";
        when(jwtServiceImpl.generateAccessToken(any(User.class))).thenReturn(accessToken);
        when(jwtServiceImpl.generateRefreshToken(any(User.class))).thenReturn(refreshToken);

        JWTResponse jwtResponse = registrationService.register(user);

        assertEquals(encodedPassword, user.getPassword());
        assertEquals(Role.ADMIN, user.getRole());

        verify(userRepository).save(user);
        assertEquals(accessToken, jwtResponse.getAccessToken());
        assertEquals(refreshToken, jwtResponse.getRefreshToken());
    }
}
