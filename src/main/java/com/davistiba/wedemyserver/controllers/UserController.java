package com.davistiba.wedemyserver.controllers;

import java.util.List;

import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/add")
    public ResponseEntity<String> addNewUser(@RequestBody User user) {
        //TODO: Add bcrypt

        try {
            userRepository.save(user);
            return new ResponseEntity<>("Created User", HttpStatus.CREATED);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.valueOf(400), ex.getMessage());
        }

    }

    @GetMapping(path = "/{id}")
    public User getUserbyId(@PathVariable(value = "id") Integer id) {
        try {
            return userRepository.findById(id).orElseThrow();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " not found");
        }

    }

    @GetMapping(path = "/find")
    public List<User> getUserbyName(@RequestParam(value = "name", defaultValue = "") String name) {
        try {
            var userList = userRepository.findByFullname(name);
            if (userList.isEmpty())
                throw new Exception("User with name" + name + "not found");
            return userList;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
