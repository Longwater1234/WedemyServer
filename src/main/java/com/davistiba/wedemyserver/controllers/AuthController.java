package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.UserDTO;
import com.davistiba.wedemyserver.models.CustomOAuthUser;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collections;
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

    @GetMapping("/statuslogin")
    public ResponseEntity<Map<String, Object>> getStatusLogin(@AuthenticationPrincipal CustomOAuthUser customOAuthUser,
                                                              @AuthenticationPrincipal User user) {
        if (customOAuthUser != null) {
            return convertToDto(customOAuthUser);
        } else if (user != null) {
            return convertToDto(user);
        } else {
            return ResponseEntity.ok().body(Collections.singletonMap("loggedIn", false));
        }
    }

    /**
     * Remove sensitive info
     *
     * @param user logged in user
     * @return user's current state
     */
    private ResponseEntity<Map<String, Object>> convertToDto(@NotNull User user) {
        UserDTO userDto = modelMapper.map(user, UserDTO.class);
        Map<String, Object> result = new HashMap<>();
        result.put("loggedIn", true);
        result.put("userInfo", userDto);
        return ResponseEntity.ok(result);
    }

}
