package com.example.prictice_task_2025.entity;

import com.example.prictice_task_2025.enumeration.Role;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String sex;
    private LocalDate dateOfBirth;
    @Enumerated(value = EnumType.STRING)
    @OneToMany(mappedBy = "user")
    private List<Order> orders;
    private Role role;
}
