package com.davistiba.wedemyserver.models;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class CustomOAuthUser extends User implements OAuth2User {

    private OAuth2User oAuth2User;

    public CustomOAuthUser(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
    }

    public String getEmail() {
        return oAuth2User.getAttribute("email");
    }
}
