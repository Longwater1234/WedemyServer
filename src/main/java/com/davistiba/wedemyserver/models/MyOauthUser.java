package com.davistiba.wedemyserver.models;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class MyOauthUser extends User implements OAuth2User {
    User user;
    OAuth2User oAuth2User;

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public String getName() {
        return user.getFullname();
    }
}
