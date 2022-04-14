package com.davistiba.wedemyserver.models;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.Map;

/**
 * custom Object for GoogleLogin User details
 */
public class CustomOAuthUser extends User implements OAuth2User, Serializable {

    private static final long serialVersionUID = -8362892628832016809L;
    private final OAuth2User oAuth2User;

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

    @Override
    public String getEmail() {
        return oAuth2User.getAttribute("email");
    }

}
