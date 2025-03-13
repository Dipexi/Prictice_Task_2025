package com.example.prictice_task_2025.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
@Getter
public enum Role implements GrantedAuthority {

    ADMIN("ADMIN"),
    USER("CUSTOMER");

    private final String vale;

    @Override
    public String getAuthority() {
        return vale;
    }

}