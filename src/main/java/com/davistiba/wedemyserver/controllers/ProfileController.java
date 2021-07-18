package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "/profile")
@PreAuthorize("hasRole('USER')")
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

    @GetMapping(path = "/me")
    public ResponseEntity<MyCustomResponse> sayHello(@AuthenticationPrincipal Principal principal) {

        if (principal != null) {
            System.out.println("principal " + principal.getName());
        }
        // System.out.println(session.getId());
        return new ResponseEntity<>(new MyCustomResponse("Hello Vue", true), HttpStatus.OK);
    }

    @GetMapping(path = "/search")
    public List<User> getUserbyName(@RequestParam(value = "name", defaultValue = "") @NotNull String name) {
        try {
            var userList = userRepository.findByFullname(name);
            if (userList.isEmpty())
                throw new Exception("User with name " + name + " not found");
            return userList;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


}
