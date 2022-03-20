package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.PasswordRequest;
import com.davistiba.wedemyserver.dto.UserDTO;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.UserRepository;
import com.davistiba.wedemyserver.service.MyUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/profile")
public class ProfileController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public final Logger logger = LoggerFactory.getLogger(String.valueOf(this));

    @Autowired
    public ProfileController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(path = "/id/{id}")
    public UserDTO getUserById(@PathVariable @NotNull Integer id) {
        try {
            return userRepository.findUserDTObyId(id).orElseThrow();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping(path = "/changepassword")
    @Secured(value = "ROLE_USER")
    public MyCustomResponse changePassword(@RequestBody @Valid PasswordRequest request, HttpSession session) {
        logger.info(request.toString());

        User user = MyUserDetailsService.getSessionUserDetails(session);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Your current password is incorrect");
        }
        if (!request.isFirstEqualSecond()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your new passwords don't match");
        }
        //else, all is OK! Update Password in DB
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return new MyCustomResponse("Your password has been updated!");
    }


}
