package com.example.prictice_task_2025.service.tests;

import com.example.prictice_task_2025.entity.User;
import com.example.prictice_task_2025.service.impl.JWTServiceImpl;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.prictice_task_2025.enumeration.Role;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceImplTest {

    private JWTServiceImpl jwtService;
    private final String secret = Base64.getEncoder()
            .encodeToString("testsecretkeytestsecretkey12345678".getBytes());
    private User user;


    @BeforeEach
    void setUp() {
        jwtService = new JWTServiceImpl(secret, secret);
        user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setFullName("Test User");
        user.setRole(Role.USER);
    }

    @Test
    void generateAccessToken_shouldReturnValidJwt() {
        String token = jwtService.generateAccessToken(user);
        assertNotNull(token);
        assertTrue(jwtService.validateAccessToken(token));

        Claims claims = jwtService.getAccessClaims(token);
        assertEquals("testuser", claims.getSubject());
        assertEquals("USER", claims.get("roles"));
        assertEquals("Test User", claims.get("fullName"));
        assertEquals(1, ((Number) claims.get("id")).longValue());
    }

    @Test
    void generateRefreshToken_shouldReturnValidJwt() {
        String token = jwtService.generateRefreshToken(user);
        assertNotNull(token);
        assertTrue(jwtService.validateRefreshToken(token));

        Claims claims = jwtService.getRefreshClaims(token);
        assertEquals("testuser", claims.getSubject());
        assertNull(claims.get("roles")); // в refresh токене нет ролей
    }

    @Test
    void validateToken_shouldReturnFalse_forInvalidSignature() {
        String fakeSecret = Base64.getEncoder().encodeToString("anotherSecretKeyWithSameLength123".getBytes());
        JWTServiceImpl wrongJwtService = new JWTServiceImpl(fakeSecret, fakeSecret);

        String token = jwtService.generateAccessToken(user);

        assertFalse(wrongJwtService.validateAccessToken(token));
    }

    @Test
    void getAccessClaims_shouldThrowException_forMalformedToken() {
        String invalidToken = "malformed.token.value";

        assertThrows(Exception.class, () -> {
            jwtService.getAccessClaims(invalidToken);
        });
    }

    @Test
    void validateToken_shouldReturnFalse_forMalformedToken() {
        String invalidToken = "malformed.token.value";
        assertFalse(jwtService.validateAccessToken(invalidToken));
    }
}
