package com.davistiba.wedemyserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {

    private static final long serialVersionUID = -1352733651057286866L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    @Size(max = 100)
    @Pattern(regexp = "^[^<>&/#]+?", message = "Invalid characters in name")
    @NotBlank
    private String fullname;

    @Column(nullable = false, unique = true)
    @Email
    @Pattern(regexp = "(^[0-9A-Za-z][\\w.-]+@[\\w]+\\.[\\w]\\S+\\w)$", message = "Invalid email!")
    @NotBlank
    private String email;

    @Column(length = 100)
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    @Transient
    @JsonProperty(access = Access.WRITE_ONLY)
    @NotBlank
    private String confirmPass;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum('LOCAL', 'GOOGLE')", nullable = false)
    private AuthProvider authProvider = AuthProvider.LOCAL;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum('ROLE_USER', 'ROLE_ADMIN')", nullable = false)
    private UserRole userRole = UserRole.ROLE_USER;

    @CreationTimestamp
    @Column(nullable = false)
    @JsonProperty(access = Access.READ_ONLY)
    private Instant datejoined;


    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(this.getUserRole());
        return Collections.singletonList(authority);
    }


    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    public String getUserRole() {
        return userRole.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!id.equals(user.id)) return false;
        if (!fullname.equals(user.fullname)) return false;
        if (!email.equals(user.email)) return false;
        if (!password.equals(user.password)) return false;
        if (!Objects.equals(confirmPass, user.confirmPass)) return false;
        return Objects.equals(datejoined, user.datejoined);
    }

    @Override
    public int hashCode() {
        int result = fullname.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }
}
