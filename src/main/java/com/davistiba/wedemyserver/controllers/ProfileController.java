package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.UserDTO;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.UserRepository;
import org.modelmapper.ModelMapper;
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

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    @Autowired
    public ProfileController(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @GetMapping(path = "/id/{id}")
    public UserDTO getUserbyId(@PathVariable(value = "id") @NotNull Integer id) {
        try {
            User user = userRepository.findById(id).orElseThrow();
            return mapper.map(user, UserDTO.class);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

}
