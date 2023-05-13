package com.davistiba.wedemyserver.config;

import com.davistiba.wedemyserver.dto.UserDTO;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.service.MyUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {
    private final MyUserDetailsService myUserDetailsService;

    private final ObjectMapper jsonMapper;

    private final ModelMapper modelMapper;

    @Value(value = "${frontend.root.url}")
    private String FRONTEND_URL;

    @Autowired
    public CustomAuthSuccessHandler(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
        this.jsonMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        this.modelMapper = new ModelMapper();
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication auth) throws IOException, ServletException {
        if (auth.getPrincipal() instanceof OidcUser) {
            //if Google Login
            OidcUser oidcUser = (OidcUser) auth.getPrincipal();
            myUserDetailsService.processOAuthPostLogin(oidcUser, request.getSession());
            response.sendRedirect(FRONTEND_URL);
            return;
        }
        User loggedInUser = (User) auth.getPrincipal();
        UserDTO userInfo = modelMapper.map(loggedInUser, UserDTO.class);

        request.getSession().setAttribute(MyUserDetailsService.USERID, userInfo.getId());
        Map<String, Object> authResponse = new HashMap<>();
        authResponse.put("success", true);
        authResponse.put("message", "Welcome back!");
        authResponse.put("userInfo", userInfo);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        response.getWriter().println(jsonMapper.writeValueAsString(authResponse));
        response.getWriter().flush();
    }


}
