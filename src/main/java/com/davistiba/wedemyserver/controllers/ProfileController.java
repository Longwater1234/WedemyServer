package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.UserDTO;
import com.davistiba.wedemyserver.dto.UserSummary;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.UserRepository;
import com.davistiba.wedemyserver.service.MyUserDetailsService;
import com.davistiba.wedemyserver.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/profile")
public class ProfileController {

    private final UserRepository userRepository;

    private final ProfileService profileService;

    @Autowired
    public ProfileController(UserRepository userRepository, ProfileService profileService) {
        this.userRepository = userRepository;
        this.profileService = profileService;
    }

    @GetMapping(path = "/mine")
    public UserDTO getUserById(@NotNull HttpSession session) {
        try {
            Integer userId = (Integer) session.getAttribute(MyUserDetailsService.USERID);
            return userRepository.findUserDTObyId(userId).orElseThrow();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping(path = "/summary")
    @ResponseStatus(value = HttpStatus.OK)
    @Cacheable(value = "usersummary", key = "#session.id")
    public List<UserSummary> getUserSummary(@NotNull HttpSession session) {

        Integer userId = (Integer) session.getAttribute(MyUserDetailsService.USERID);
        User user = userRepository.findById(userId).orElseThrow();
        return profileService.getUserSummaryList(user);

    }

}
