package com.davistiba.wedemyserver.config;

import com.davistiba.wedemyserver.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom handling of Login by JSON Post request
 */
@Component
@WebFilter(filterName = "CustomLoginHandler")
public class CustomLoginHandler extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    @Autowired
    public CustomLoginHandler(AuthenticationManager authManager, AuthenticationSuccessHandler successHandler) {
        super(authManager);
        this.setFilterProcessesUrl("/auth/login");
        this.setAuthenticationSuccessHandler(successHandler);
        this.objectMapper = new JsonMapper();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            String password = loginRequest.getPassword();
            String email = loginRequest.getEmail();
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, password);
            return this.getAuthenticationManager().authenticate(auth);
        } catch (Exception e) {
            logger.error("Login error:" + e.getMessage());
            throw new AuthenticationServiceException(e.getLocalizedMessage());
        }
    }

}

