package com.example.prictice_task_2025.controller;

import com.example.prictice_task_2025.dto.UserDto;
import com.example.prictice_task_2025.entity.User;
import com.example.prictice_task_2025.security.JWTRequest;
import com.example.prictice_task_2025.security.JWTResponse;
import com.example.prictice_task_2025.security.RefreshJWTRequest;
import com.example.prictice_task_2025.service.AuthService;
import com.example.prictice_task_2025.service.RegistrationService;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin()
public class AuthController {

    private final AuthService authService;
    private final ModelMapper modelMapper;
    private final RegistrationService registrationService;

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@RequestBody @Valid JWTRequest authRequest) throws AuthException {
        final JWTResponse token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/token")
    public ResponseEntity<JWTResponse> getNewAccessToken(@RequestBody RefreshJWTRequest request) {
        final JWTResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JWTResponse> getNewRefreshToken(@RequestBody RefreshJWTRequest request) throws AuthException {
        final JWTResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/registration")
    public ResponseEntity<JWTResponse> registration(@RequestBody @Valid UserDto userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        JWTResponse register = registrationService.register(user);
        return ResponseEntity.ok(register);
    }
}