package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "/profile")
@Secured("ROLE_USER")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    final Logger logger = LoggerFactory.getLogger(String.valueOf(this));

    @GetMapping(path = "/id/{id}")
    public User getUserbyId(@PathVariable(value = "id") @NotNull Integer id) {
        try {
            return userRepository.findById(id).orElseThrow();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " not found");
        }
    }

    @GetMapping(path = "/me")
    public ResponseEntity<MyCustomResponse> sayHello(Principal principal, HttpSession session) {
        //this returns the currently logged in user

        session.getAttributeNames().asIterator().forEachRemaining(System.out::println);
        logger.info("principal " + principal.getName());
        //use this email to do further queries from DB.

        return new ResponseEntity<>(new MyCustomResponse("Hello Vue", true), HttpStatus.OK);
    }

    @GetMapping(path = "/search")
    public List<User> getUserbyName(@RequestParam(value = "name") @NotBlank String name) {
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
