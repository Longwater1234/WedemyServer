package com.davistiba.wedemyserver.models;

import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Map;

/**
 * [NOT A TABLE]
 * custom Object for GoogleLogin User details
 */
public class CustomOAuthUser extends User implements OidcUser, Serializable {

    private static final long serialVersionUID = -8362892628832016809L;
    private final OidcUser oidcUser;

    public CustomOAuthUser(OidcUser oidcUser) {
        this.oidcUser = oidcUser;
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    @Override
    @Transient
    public Map<String, Object> getClaims() {
        return oidcUser.getClaims();
    }

    @Override
    @Transient
    public OidcUserInfo getUserInfo() {
        return oidcUser.getUserInfo();
    }

    @Override
    @Transient
    public OidcIdToken getIdToken() {
        return oidcUser.getIdToken();
    }

    @Override
    @Transient
    public Map<String, Object> getAttributes() {
        return oidcUser.getAttributes();
    }

    @Override
    @Transient
    public String getUsername() {
        return oidcUser.getEmail();
    }

    @Override
    @Transient
    public String getName() {
        return oidcUser.getName();
    }

    @Override
    @Transient
    public String getEmail() {
        return oidcUser.getEmail();
    }
}
