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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom handling of Login by JSON Post request to <code>/auth/login</code>
 */
@Component
@WebFilter(filterName = "CustomLoginHandler")
public class CustomLoginHandler extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();
    private final SecurityContextRepository contextRepository = new DelegatingSecurityContextRepository(
            new HttpSessionSecurityContextRepository(),
            new RequestAttributeSecurityContextRepository()
    );

    @Autowired
    public CustomLoginHandler(AuthenticationManager authManager, AuthenticationSuccessHandler successHandler) {
        super(authManager);
        this.setFilterProcessesUrl("/auth/login");
        this.objectMapper = new JsonMapper();
        this.setAuthenticationSuccessHandler(successHandler);
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

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // new in Springboot 3, must explicitly save SecurityContext, not auto :-(
        SecurityContext context = strategy.getContext();
        strategy.getContext().setAuthentication(authResult);
        contextRepository.saveContext(context, request, response);
        super.successfulAuthentication(request, response, chain, authResult);
    }
}

