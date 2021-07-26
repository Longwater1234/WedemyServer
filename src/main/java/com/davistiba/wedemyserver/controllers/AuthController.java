package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping(path = "/register")
    public ResponseEntity<MyCustomResponse> addNewUser(@RequestBody @Valid User user) {
        // TODO: ADD Session
        if (!user.getPassword().equals(user.getConfirmPass()))
            throw new ResponseStatusException(HttpStatus.valueOf(422), "Passwords dont match");

        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new MyCustomResponse("Registered", true));
        } catch (Exception ex) {
            if (ex instanceof DataIntegrityViolationException) {
                throw new ResponseStatusException(HttpStatus.valueOf(409), "User already exists");
            }
            throw new ResponseStatusException(HttpStatus.valueOf(400), ex.getMessage());
        }
    }

    @GetMapping(path = "/hello")
    @ResponseStatus(value = HttpStatus.OK)
    public Map<String, Object> sayHello() {

        Map<String, Object> mama = new HashMap<>();
        mama.put("message", "hi");
        mama.put("success", true);

        return mama;
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MyCustomResponse> loginUser(@RequestBody User user, HttpSession session) {
        // TODO: ADD Session
        try {
            // if found, fetch his password.
            // then check if password matches with Bcrypt.
            // if true, set cookies. else throw
            var UserDB = userRepository.findByEmail(user.getEmail())
                    .orElseThrow(() -> new Exception("User not found"));

            if (bCryptPasswordEncoder.matches(user.getPassword(), UserDB.getPassword())) {
                session.setAttribute("USER_ID", UserDB.getUserId());
                return ResponseEntity.status(200).body(new MyCustomResponse("Logged in", true));
            } else
                throw new Exception("Wrong email or password");

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.valueOf(401), e.getMessage());
        }
    }

}
