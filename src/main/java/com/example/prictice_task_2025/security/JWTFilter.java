package com.example.prictice_task_2025.security;

import com.example.prictice_task_2025.entity.User;
import com.example.prictice_task_2025.service.impl.JWTServiceImpl;
import com.example.prictice_task_2025.service.impl.UserServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class JWTFilter extends GenericFilterBean {

    private static final String AUTHORIZATION = "Authorization";

    private final JWTServiceImpl jwtServiceImpl;
    private final UserServiceImpl userService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
            throws IOException, ServletException {
        final String token = getTokenFromRequest((HttpServletRequest) request);
        if (token != null && jwtServiceImpl.validateAccessToken(token)) {
            final Claims claims = jwtServiceImpl.getAccessClaims(token);
            final JWTAuthentication jwtInfoToken = JWTUtils.generate(claims);
            jwtInfoToken.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
        }
        fc.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

}