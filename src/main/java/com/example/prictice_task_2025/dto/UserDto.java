package com.example.prictice_task_2025.dto;
import com.example.prictice_task_2025.enumeration.Role;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UserDto {
    @NotEmpty
    private String username;
    @NotEmpty
    private String email;
    @NotEmpty
    @Size(min = 3, message = "Length must be more than 3")
    private String password;
    @NotEmpty
    private String fullName;
    @NotEmpty
    private String sex;
    private LocalDate localDate;
    private Role role;
}
