package com.example.prictice_task_2025.controller;

import com.example.prictice_task_2025.dto.UserDto;
import com.example.prictice_task_2025.entity.User;
import com.example.prictice_task_2025.service.RegistrationService;
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

    private final ModelMapper modelMapper;
    private final RegistrationService registrationService;

    @PostMapping("/registration")
    public ResponseEntity<User> registration(@RequestBody @Valid UserDto userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        User registeredUser = registrationService.register(user);
        return ResponseEntity.ok(registeredUser);
    }
}