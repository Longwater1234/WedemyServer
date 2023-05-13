package com.davistiba.wedemyserver.config;

import com.davistiba.wedemyserver.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Custom handling of Login by JSON Post request
 */
@Component
@WebFilter(filterName = "CustomLoginHandler")
public class CustomLoginHandler extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    private final AuthenticationSuccessHandler successHandler;

    @Autowired
    public CustomLoginHandler(AuthenticationManager authManager, AuthenticationSuccessHandler successHandler) {
        super(authManager);
        this.setFilterProcessesUrl("/auth/login");
        this.objectMapper = new JsonMapper();
        this.successHandler = successHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            String password = loginRequest.getPassword();
            String email = loginRequest.getEmail();
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, password);
            this.setAuthenticationSuccessHandler(successHandler);
            return this.getAuthenticationManager().authenticate(auth);
        } catch (Exception e) {
            logger.error("Login error:" + e.getMessage());
            throw new AuthenticationServiceException(e.getLocalizedMessage());
        }
    }
}

