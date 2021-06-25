package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping(path = "/register")
    @Validated
    public ResponseEntity<MyCustomResponse> addNewUser(@RequestBody User user) {
        // TODO: ADD Session
        try {
            if (user.getPassword().isBlank() || user.getEmail().isBlank() || user.getFullname().isBlank())
                throw new Exception("Must fill all fields");

            if (!user.getEmail().matches("(^[0-9A-Za-z][\\w.-]+@[\\w]+\\.[\\w]\\S+\\w)$")) {
                throw new Exception("Email is invalid!");
            }
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return new ResponseEntity<>(new MyCustomResponse("Registered :)", true),
                    HttpStatus.CREATED);
        } catch (Exception ex) {
            if (ex instanceof DataIntegrityViolationException) {
                throw new ResponseStatusException(HttpStatus.valueOf(409), "User already exists");
            }
            throw new ResponseStatusException(HttpStatus.valueOf(400), ex.getMessage());
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<MyCustomResponse> loginUser(@RequestBody User user) {
        // TODO: ADD Session + Cookies
        try {
            // if found, fetch his password.
            // then check if password matches with Bcrypt.
            // if true, set cookies. else throw
            var UserDB = userRepository.findByEmail(user.getEmail())
                    .orElseThrow(() -> new Exception("User not found"));

            if (bCryptPasswordEncoder.matches(user.getPassword(), UserDB.getPassword())) {
                return new ResponseEntity<>(new MyCustomResponse("logged in", true), HttpStatus.OK);
            } else
                throw new Exception("Wrong email or password");

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(401), e.getMessage());
        }
    }
}
