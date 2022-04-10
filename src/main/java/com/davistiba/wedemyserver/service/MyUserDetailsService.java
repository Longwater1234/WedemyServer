package com.davistiba.wedemyserver.service;

import com.davistiba.wedemyserver.models.AuthProvider;
import com.davistiba.wedemyserver.models.CustomOAuthUser;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.models.UserRole;
import com.davistiba.wedemyserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.Optional;


@Service
public class MyUserDetailsService implements UserDetailsService {

    public static final String USERID = "USER_ID";
    public static final String SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));
    }

    /**
     * Checks if Google-User exists in dB. If not, register as new user.
     * Else just login with new Session.
     *
     * @param oAuth2User authenticated User
     * @param session    logged-in session
     */
    public void processOAuthPostLogin(OAuth2User oAuth2User, HttpSession session) {
        CustomOAuthUser m = new CustomOAuthUser(oAuth2User);
        Optional<User> existUser = userRepository.findByEmail(m.getEmail());

        if (existUser.isEmpty()) {
            User newUser = new User();
            newUser.setFullname(m.getName());
            newUser.setEmail(m.getEmail());
            newUser.setConfirmPass("WHATEVER!");
            newUser.setAuthProvider(AuthProvider.GOOGLE);
            newUser.setUserRole(UserRole.ROLE_USER);

            userRepository.save(newUser);
            session.setAttribute(USERID, newUser.getId());
            return;
        }
        Integer userId = existUser.get().getId();
        session.setAttribute(USERID, userId);

    }


    /**
     * Custom method to get User details from SESSION STORE (redis)
     *
     * @param session loggedIn session
     * @return USER object
     */
    public static User getSessionUserDetails(@NotNull HttpSession session) {
        SecurityContext context = (SecurityContext) session.getAttribute(SECURITY_CONTEXT);
        return (User) context.getAuthentication().getPrincipal();
    }


}