package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/add")
    public ResponseEntity<String> addNewUser(@RequestBody User user) {

        // if (!user.getEmail().matches("(^([0-9A-Za-z])[\\w\\.\\-]+@[\\w]+\\.[\\w]\\S+)$")) {
        //     return new ResponseEntity<>("Email is invalid", HttpStatus.valueOf(422));
        // }
        // if (user.getFullname().isBlank() || user.getEmail().isBlank() || user.getPassword().isBlank()) {
        //     return new ResponseEntity<>("Must fill all fields ", HttpStatus.valueOf(422));
        // }

        try {
            userRepository.save(user);
            return new ResponseEntity<>("Created User", HttpStatus.CREATED);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), ex.getMessage());
        }

    }

    @GetMapping(path = "/all")
    public Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }
}
