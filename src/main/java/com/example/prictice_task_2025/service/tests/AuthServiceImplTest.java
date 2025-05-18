package com.example.prictice_task_2025.service.tests;

import java. util. Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import com.example.prictice_task_2025.entity.User;
import com.example.prictice_task_2025.security.JWTRequest;
import com.example.prictice_task_2025.security.JWTResponse;
import com.example.prictice_task_2025.service.impl.AuthServiceImpl;
import com.example.prictice_task_2025.service.impl.JWTServiceImpl;
import com.example.prictice_task_2025.service.impl.UserServiceImpl;

import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock private UserServiceImpl userService;
    @Mock private JWTServiceImpl jwtService;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private AuthServiceImpl authService;

    private User user;
    private JWTRequest jwtRequest;

    @BeforeEach
    void setup() {
        user = new User();
        user.setUsername("john");
        user.setPassword("encoded_password");

        jwtRequest = new JWTRequest("john", "raw_password");
    }

    @Test
    void login_shouldReturnTokens_whenPasswordIsCorrect() throws AuthException {
        when(userService.findByUsername("john")).thenReturn(user);
        when(passwordEncoder.matches("raw_password", "encoded_password")).thenReturn(true);
        when(jwtService.generateAccessToken(user)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(user)).thenReturn("refresh-token");

        JWTResponse response = authService.login(jwtRequest);

        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
    }

    @Test
    void login_shouldThrowAuthException_whenPasswordIsIncorrect() {
        when(userService.findByUsername("john")).thenReturn(user);
        when(passwordEncoder.matches("raw_password", "encoded_password")).thenReturn(false);

        assertThrows(AuthException.class, () -> authService.login(jwtRequest));
    }

    @Test
    void getAccessToken_shouldReturnNewAccessToken_whenRefreshTokenIsValid() throws Exception {
        Claims claims = mock(Claims.class);
        when(jwtService.validateRefreshToken("valid-refresh")).thenReturn(true);
        when(jwtService.getRefreshClaims("valid-refresh")).thenReturn(claims);
        when(claims.getSubject()).thenReturn("john");

        when(userService.findByUsername("john")).thenReturn(user);
        when(jwtService.generateAccessToken(user)).thenReturn("new-access");

        var field = AuthServiceImpl.class.getDeclaredField("refreshStorage");
        field.setAccessible(true);
        Map<String, String> refreshStorage = (Map<String, String>) field.get(authService);
        refreshStorage.put("john", "valid-refresh");

        JWTResponse response = authService.getAccessToken("valid-refresh");

        assertEquals("new-access", response.getAccessToken());
        assertNull(response.getRefreshToken());
    }


    @Test
    void refresh_shouldReturnNewTokens_whenValidRefreshToken() throws Exception {
        Claims claims = mock(Claims.class);
        when(jwtService.validateRefreshToken("valid-refresh")).thenReturn(true);
        when(jwtService.getRefreshClaims("valid-refresh")).thenReturn(claims);
        when(claims.getSubject()).thenReturn("john");

        when(userService.findByUsername("john")).thenReturn(user);
        when(jwtService.generateAccessToken(user)).thenReturn("new-access");
        when(jwtService.generateRefreshToken(user)).thenReturn("new-refresh");

        var field = AuthServiceImpl.class.getDeclaredField("refreshStorage");
        field.setAccessible(true);
        Map<String, String> refreshStorage = (Map<String, String>) field.get(authService);
        refreshStorage.put("john", "valid-refresh");

        JWTResponse response = authService.refresh("valid-refresh");

        assertEquals("new-access", response.getAccessToken());
        assertEquals("new-refresh", response.getRefreshToken());
    }


    @Test
    void refresh_shouldThrowException_whenInvalidRefreshToken() {
        when(jwtService.validateRefreshToken("invalid")).thenReturn(false);
        assertThrows(AuthException.class, () -> authService.refresh("invalid"));
    }
}
