package com.davistiba.wedemyserver.config;

import com.davistiba.wedemyserver.controllers.AuthController;
import com.davistiba.wedemyserver.models.AuthProvider;
import com.davistiba.wedemyserver.models.CustomOAuthUser;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.models.UserRole;
import com.davistiba.wedemyserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;


@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));
    }

    /**
     * Checks if OAuth User exists in dB. If not, register new user.
     * Else just login with new Session.
     *
     * @param oAuth2User authenticated User
     */
    public void processOAuthPostLogin(OAuth2User oAuth2User, HttpSession session) {
        CustomOAuthUser m = new CustomOAuthUser(oAuth2User);
        Optional<User> existUser = userRepository.findByEmail(m.getEmail());

        if (existUser.isEmpty()) {
            User newUser = new User();
            newUser.setFullname(m.getName());
            newUser.setEmail(m.getEmail());
            newUser.setConfirmPass("haha");
            newUser.setAuthProvider(AuthProvider.GOOGLE);
            newUser.setUserRole(UserRole.ROLE_USER);

            userRepository.save(newUser);
            session.setAttribute(AuthController.USERID, newUser.getId());
            return;
        }
        Integer userId = existUser.get().getId();
        session.setAttribute(AuthController.USERID, userId);

    }

}
