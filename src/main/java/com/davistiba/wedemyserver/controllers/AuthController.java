package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.CustomOAuthUser;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.UserRepository;
import com.davistiba.wedemyserver.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/auth")
@CrossOrigin
public class AuthController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping(path = "/register")
    public ResponseEntity<MyCustomResponse> addNewUser(@RequestBody @Valid User user) {
        if (!user.getPassword().equals(user.getConfirmPass()))
            throw new ResponseStatusException(HttpStatus.valueOf(422), "Passwords dont match");

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new MyCustomResponse("Registered! Welcome", true));
        } catch (Exception ex) {
            if (ex instanceof DataIntegrityViolationException) {
                throw new ResponseStatusException(HttpStatus.valueOf(409), "Account already exists");
            }
            throw new ResponseStatusException(HttpStatus.valueOf(400), ex.getMessage());
        }
    }


    @GetMapping(path = "/statuslogin")
    public ResponseEntity<Object> checkLoginStatus(Authentication auth,
                                                   @AuthenticationPrincipal OidcUser oidcUser) {
        Map<String, Object> response = new HashMap<>();

        if (oidcUser != null) {
            CustomOAuthUser cu = new CustomOAuthUser(oidcUser);
            response.put("user", cu.getName());
            response.put("loggedIn", true);

        } else if (auth != null) {
            response.put("user", auth.getPrincipal());
            response.put("loggedIn", auth.isAuthenticated());

        } else {
            response.put("user", "");
            response.put("loggedIn", false);
        }
        return ResponseEntity.ok().body(response);
    }


    @PostMapping(path = "/login")
    @Secured(value = "ROLE_USER")
    // this is the target LOGIN URL, but actually runs AFTER successful basicAuth login
    public ResponseEntity<Object> realBasicAuthEntry(HttpSession session, Authentication auth) {
        Map<String, Object> response = new HashMap<>();
        try {
            User loggedInUser = userRepository.findByEmail(auth.getName()).orElseThrow();
            Integer userId = loggedInUser.getId();
            session.setAttribute(MyUserDetailsService.USERID, userId);

            //return response
            response.put("success", auth.isAuthenticated());
            response.put("message", "Logged in!");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


}
