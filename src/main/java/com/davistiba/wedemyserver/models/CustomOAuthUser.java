package com.davistiba.wedemyserver.models;

import lombok.ToString;
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
@ToString
public class CustomOAuthUser extends User implements OidcUser, Serializable {

    private static final long serialVersionUID = -8362892628832016809L;
    private final OidcUser oidcUser;

    public CustomOAuthUser(OidcUser oidcUser) {
        this.oidcUser = oidcUser;
    }

    @Override
    @Transient
    public Map<String, Object> getClaims() {
        return oidcUser.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUser.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcUser.getIdToken();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oidcUser.getAttributes();
    }

    @Override
    public String getUsername() {
        return oidcUser.getAttribute("name");
    }

    @Override
    public String getName() {
        return oidcUser.getAttribute("name");
    }

    @Override
    public String getEmail() {
        return oidcUser.getAttribute("email");
    }
}
