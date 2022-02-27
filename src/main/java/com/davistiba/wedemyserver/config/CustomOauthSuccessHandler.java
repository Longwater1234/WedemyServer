package com.davistiba.wedemyserver.config;

import com.davistiba.wedemyserver.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomOauthSuccessHandler implements AuthenticationSuccessHandler {
    final MyUserDetailsService myUserDetailsService;

    @Autowired
    public CustomOauthSuccessHandler(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @Value(value = "${frontend.root.url}")
    private String FRONTEND_URL;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        myUserDetailsService.processOAuthPostLogin(oAuth2User, request.getSession());
        response.sendRedirect(FRONTEND_URL);

    }
}
