package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/auth")
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
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Passwords don't match");

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(new MyCustomResponse("Registered! Welcome"));
        } catch (Exception ex) {
            if (ex instanceof DataIntegrityViolationException) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Account already exists");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }


    @GetMapping(path = "/statuslogin")
    //TODO FIX THIS MESS. SIMPLY RETURN 'TRUE'
    public ResponseEntity<Map<String, Object>> checkLoginStatus(Authentication auth, /* username+pass */
                                                                @AuthenticationPrincipal OAuth2User oAuth2User /* Google */) {
        Map<String, Object> response = new HashMap<>();
        String fullname = "";
        boolean loggedIn = false;
        if (oAuth2User != null) {
            fullname = oAuth2User.getAttribute("name");
            loggedIn = true;
        } else if (auth != null) {
            User u = (User) auth.getPrincipal();
            fullname = u.getFullname();
            loggedIn = auth.isAuthenticated();
        }
        response.put("fullname", fullname);
        response.put("loggedIn", loggedIn);
        return ResponseEntity.ok().body(response);
    }


//    @PostMapping(path = "/login")
//    @Secured(value = "ROLE_STUDENT")
//    // Basic Auth login should target this endpoint.
//    public ResponseEntity<Map<String, Object>> realBasicAuthEntry(HttpSession session, Authentication auth) {
//        Map<String, Object> response = new HashMap<>();
//        try {
//            UserDTO loggedInUser = userRepository.findUserDTObyEmail(auth.getName()).orElseThrow();
//            Integer userId = loggedInUser.getId();
//            session.setAttribute(MyUserDetailsService.USERID, userId);
//            //return response
//            response.put("success", auth.isAuthenticated());
//            response.put("message", "Logged in!");
//            response.put("userInfo", loggedInUser);
//            return ResponseEntity.ok().body(response);
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        }
//    }


}
