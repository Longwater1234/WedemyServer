package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.config.MainUserDetails;
import com.davistiba.wedemyserver.dto.LoginStatus;
import com.davistiba.wedemyserver.dto.RegisterRequest;
import com.davistiba.wedemyserver.dto.UserDTO;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.UserRepository;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<MyCustomResponse> addNewUser(@RequestBody @Valid RegisterRequest request) {
        try {
            User user = modelMapper.map(request, User.class);
            if (!user.getPassword().equals(user.getConfirmPass())) {
                throw new IllegalArgumentException("Passwords don't match");
            }
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

    @GetMapping("/status")
    public ResponseEntity<LoginStatus> getStatusLogin(Authentication auth) {
        if (auth != null) {
            MainUserDetails userDetails = (MainUserDetails) auth.getPrincipal();
            User user = userDetails.getUser();
            return convertToDto(user);
        } else {
            return ResponseEntity.ok().body(new LoginStatus());
        }
    }

    /**
     * Remove sensitive info
     *
     * @param user logged in user
     * @return user's current state
     */
    private ResponseEntity<LoginStatus> convertToDto(@NotNull User user) {
        UserDTO userDto = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.ok().body(new LoginStatus(true, userDto));
    }

}
