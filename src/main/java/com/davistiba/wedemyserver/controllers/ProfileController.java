package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/profile")
@Secured("ROLE_USER")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/id/{id}")
    public User getUserbyId(@PathVariable(value = "id") @NotNull Integer id) {
        try {
            return userRepository.findById(id).orElseThrow();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " not found");
        }
    }

    @GetMapping(path = "/search")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUserbyName(@RequestParam(value = "name") @NotBlank String name) {
        var userList = userRepository.findByFullname(name);
        if (userList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with name " + name + " not found");
        }
        return userList;

    }


}
