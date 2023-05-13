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
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.Optional;


@Service
public class MyUserDetailsService implements UserDetailsService {

    public static final String USERID = "USER_ID";
    private static final String SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

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
     * @param oidcUser authenticated User
     * @param session  logged-in session
     */
    public void processOAuthPostLogin(OidcUser oidcUser, HttpSession session) {
        CustomOAuthUser m = new CustomOAuthUser(oidcUser);
        Optional<User> existUser = userRepository.findByEmail(m.getEmail());

        if (existUser.isEmpty()) {
            User newUser = new User();
            newUser.setFullname(m.getName());
            newUser.setEmail(m.getEmail());
            newUser.setConfirmPass("WHATEVER!"); //<-- doesn't matter for G-login
            newUser.setAuthProvider(AuthProvider.GOOGLE);
            newUser.setUserRole(UserRole.ROLE_STUDENT);

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
    public static User getSessionUserInfo(@NotNull HttpSession session) {
        SecurityContext context = (SecurityContext) session.getAttribute(SECURITY_CONTEXT);
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof CustomOAuthUser) {
            return (CustomOAuthUser) principal;
        }
        return (User) principal;
    }

    /**
     * Just return the user_id saved in Redis Store
     *
     * @param session session
     * @return userId
     */
    public static Integer getSessionUserId(@NotNull HttpSession session) {
        return (Integer) session.getAttribute(USERID);
    }

}
