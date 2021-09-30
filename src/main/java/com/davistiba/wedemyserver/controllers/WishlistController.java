package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.WishlistDTO;
import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.models.Wishlist;
import com.davistiba.wedemyserver.repository.CourseRepository;
import com.davistiba.wedemyserver.repository.UserRepository;
import com.davistiba.wedemyserver.repository.WishlistRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Secured("ROLE_USER")
@RequestMapping(path = "/wishlist")
public class WishlistController {

    @Autowired
    WishlistRepository wishlistRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ModelMapper mapper;

    @Autowired
    Logger logger;

    @PostMapping(path = "/course/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public MyCustomResponse addNewWishlist(@PathVariable(value = "courseId") Integer courseId,
                                           Principal principal) {

        try {
            User myUser = userRepository.findByEmail(principal.getName()).orElseThrow();
            Course myCourse = courseRepository.findById(courseId).orElseThrow();
            wishlistRepository.save(new Wishlist(myUser, myCourse));

            return new MyCustomResponse("Added to Wishlist: courseId " + myCourse.getCourseId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(path = "/mine/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Boolean> getIfUserLikedCourse(@PathVariable(value = "courseId") @NotNull Integer courseId,
                                                     Principal principal) {

        try {
            User myUser = userRepository.findByEmail(principal.getName()).orElseThrow();
            logger.info("STATUS CHECK");

            var wishlist = wishlistRepository.checkIfWishlistExists(courseId, myUser.getUserId());
            Map<String, Boolean> response = new HashMap<>();
            response.put("isWishlist", wishlist.isPresent());
            return response;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(path = "/mine")
    @ResponseStatus(HttpStatus.OK)
    public List<WishlistDTO> getAllMyWishlistCourses(Principal principal) {
        var WishlistItems = wishlistRepository.getWishlistsByUser_Email(principal.getName());

        if (WishlistItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Your wishlist is empty!");
        }
        return WishlistItems.stream().map(item -> mapper.map(item, WishlistDTO.class))
                .collect(Collectors.toList());
    }

    @DeleteMapping(path = "/course/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public MyCustomResponse removeWishlist(@PathVariable(value = "courseId") Integer courseId,
                                           Principal principal) {

        try {
            logger.info("i am running below");
            User myUser = userRepository.findByEmail(principal.getName()).orElseThrow();
            wishlistRepository.deleteWishlistByCourseIdAndUserId(courseId, myUser.getUserId());
            return new MyCustomResponse("Removed from Wishlist course: " + courseId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
