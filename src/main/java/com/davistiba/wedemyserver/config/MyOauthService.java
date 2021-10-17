package com.davistiba.wedemyserver.config;

import com.davistiba.wedemyserver.models.MyOauthUser;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class MyOauthService implements OAuth2UserService<OAuth2UserRequest, MyOauthUser> {

    MyOauthUser myOauthUser;

    @Override
    public MyOauthUser loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        return myOauthUser;
    }
}
