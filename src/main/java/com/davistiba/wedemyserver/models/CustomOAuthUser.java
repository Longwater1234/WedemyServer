package com.davistiba.wedemyserver.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * [NOT A TABLE]
 * Custom Object for GoogleLogin User details
 */
public class CustomOAuthUser extends User implements OidcUser, Serializable {

    @Serial
    private static final long serialVersionUID = -8362892628832016809L;
    private final OidcUser oidcUser;

    public CustomOAuthUser(OidcUser oidcUser) {
        this.oidcUser = oidcUser;
    }

    @Override
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
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(super.getUserRole());
        return Collections.singletonList(authority);
    }

    @Override
    public String getFullname() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CustomOAuthUser that = (CustomOAuthUser) o;
        return Objects.equals(oidcUser.getEmail(), that.oidcUser.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), oidcUser);
    }
}
