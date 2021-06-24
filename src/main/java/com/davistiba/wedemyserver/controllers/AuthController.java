package com.davistiba.wedemyserver.controllers;

import java.security.SecureRandom;

import javax.validation.Valid;

import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10, new SecureRandom());

    @PostMapping(path = "/register")
    public ResponseEntity<String> addNewUser(@RequestBody @Valid User user) {
        // TODO: Add bcrypt
        // TODO: ADD Session

        if (user.getPassword().isBlank() || user.getFullname().isBlank() || user.getFullname().isBlank())
            return new ResponseEntity<>("Must fill all fields", HttpStatus.BAD_REQUEST);

        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return new ResponseEntity<>("Created User", HttpStatus.CREATED);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), ex.getMessage());
        }
    }

    @PostMapping(path = "/loginaa")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        // TODO: Add bcrypt
        // TODO: ADD Session + Cookies
        try {
            // if found, fetch his password.
            // then check if password matches with Bcrypt.
            // if true, set cookies. else throw
            var myUser = userRepository.findByEmail(user.getUsername())
                    .orElseThrow(() -> new Exception("User not found"));
            if (bCryptPasswordEncoder.matches(user.getPassword(), myUser.getPassword())) {
                return new ResponseEntity<>("Logged in", HttpStatus.OK);
            } else
                throw new Exception("Wrong email or password");

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(401), e.getMessage());
        }
    }
}
