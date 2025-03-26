package com.example.prictice_task_2025.security;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.example.prictice_task_2025.enumeration.Role;
import org.springframework.stereotype.Component;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public final class JWTUtils {

    public static JWTAuthentication generate(Claims claims) {
        final JWTAuthentication jwtInfoToken = new JWTAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setFullName(claims.get("fullName", String.class));
        jwtInfoToken.setUsername(claims.getSubject());
        return jwtInfoToken;
    }

    private static Set<Role> getRoles(Claims claims) {
        final String rolesString = claims.get("roles", String.class);
        final List<String> roles = Arrays.asList(rolesString.split(","));
        return roles.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }

}
