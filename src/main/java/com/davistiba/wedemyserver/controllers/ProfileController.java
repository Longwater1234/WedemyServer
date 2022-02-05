package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.UserDTO;
import com.davistiba.wedemyserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/profile")
public class ProfileController {

    private final UserRepository userRepository;


    @Autowired
    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/id/{id}")
    public UserDTO getUserById(@PathVariable @NotNull Integer id) {
        try {
            return userRepository.findUserDTObyId(id).orElseThrow();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

}
