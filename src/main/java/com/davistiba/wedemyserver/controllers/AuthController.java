package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.CustomOAuthUser;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    @Autowired
    public AuthController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = new ModelMapper();
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
    //TODO FIX THIS MESS. SIMPLY RETURN 'TRUE' and userDto
    public ResponseEntity<Map<String, Object>> checkLoginStatus(Authentication auth, /* username+pass */
                                                                @AuthenticationPrincipal CustomOAuthUser oAuth2User /* Google */) {
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

    /*

     @GetMapping("/status-login")
    public ResponseEntity<UserDto> getStatusLogin(Authentication authentication) {
    Object principal = authentication.getPrincipal();

    if (principal instanceof CustomOAuthUser) {
        CustomOAuthUser customOAuthUser = (CustomOAuthUser) principal;
        UserDto userDto = new UserDto();
        userDto.setId(customOAuthUser.getId());
        userDto.setName(customOAuthUser.getName());
        userDto.setEmail(customOAuthUser.getEmail());
        return ResponseEntity.ok(userDto);
    } else if (principal instanceof User) {
        User user = (User) principal;
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getUsername());
        userDto.setEmail(user.getEmail());
        return ResponseEntity.ok(userDto);
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    }

     */
}
