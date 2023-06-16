package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.StudentSummary;
import com.davistiba.wedemyserver.dto.UserDTO;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.UserRepository;
import com.davistiba.wedemyserver.service.MyUserDetailsService;
import com.davistiba.wedemyserver.service.ProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController {

    private final UserRepository userRepository;

    private final ProfileService profileService;

    private final ModelMapper modelMapper;

    @Autowired
    public ProfileController(UserRepository userRepository, ProfileService profileService) {
        this.userRepository = userRepository;
        this.profileService = profileService;
        this.modelMapper = new ModelMapper();
    }

    @GetMapping(path = "/mine")
    public ResponseEntity<UserDTO> getUserById(@NotNull HttpSession session) {
        try {
            Integer userId = (Integer) session.getAttribute(MyUserDetailsService.USERID);
            UserDTO userDTO = userRepository.findUserDTObyId(userId).orElseThrow();
            return ResponseEntity.ok().body(userDTO);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PutMapping(path = "/mine")
    @Transactional
    public ResponseEntity<UserDTO> editMyProfile(@RequestBody UserDTO userDTO, @NotNull HttpSession session) {
        try {
            Integer userId = (Integer) session.getAttribute(MyUserDetailsService.USERID);
            User u = userRepository.findById(userId).orElseThrow();
            u.setFullname(userDTO.getFullname());
            // You may modify other fields
            u.setConfirmPass("WHATEVER!");
            User freshUser = userRepository.save(u);
            return ResponseEntity.ok().body(modelMapper.map(freshUser, UserDTO.class));
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not edit your profile", ex);
        }
    }


    @GetMapping(path = "/summary")
    @ResponseStatus(value = HttpStatus.OK)
    @Cacheable(value = "student-summary", key = "#session.id")
    public List<StudentSummary> getUserSummary(@NotNull HttpSession session) {
        Integer userId = (Integer) session.getAttribute(MyUserDetailsService.USERID);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return profileService.getUserSummaryList(user);
    }

}
