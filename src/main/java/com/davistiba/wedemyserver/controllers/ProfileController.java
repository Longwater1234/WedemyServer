package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/profile")
@Secured({"ROLE_USER", "ROLE_ADMIN"})
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/id/{id}")
    public User getUserbyId(@PathVariable(value = "id") @NotNull Integer id) {
        try {
            // TODO: use DTO here
            return userRepository.findById(id).orElseThrow();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " not found");
        }
    }

}
