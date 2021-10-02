package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.models.Wishlist;
import com.davistiba.wedemyserver.repository.CourseRepository;
import com.davistiba.wedemyserver.repository.UserRepository;
import com.davistiba.wedemyserver.repository.WishlistRepository;
import org.modelmapper.ModelMapper;
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

    @PostMapping(path = "/course/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public MyCustomResponse addNewWishlist(@PathVariable(value = "courseId") Integer courseId,
                                           Principal principal) {

        try {
            User myUser = userRepository.findByEmail(principal.getName()).orElseThrow();
            Course myCourse = courseRepository.findById(courseId).orElseThrow();
            wishlistRepository.save(new Wishlist(myUser, myCourse));

            return new MyCustomResponse("Added to Wishlist: courseId " + myCourse.getId());
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
            var wishlist = wishlistRepository.checkIfWishlistExists(courseId, myUser.getId());
            Map<String, Boolean> response = new HashMap<>();
            response.put("isWishlist", wishlist.isPresent());
            return response;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(path = "/mine")
    @ResponseStatus(HttpStatus.OK)
    public List<Wishlist> getAllMyWishlistCourses(Principal principal) {

        try {
            User user = userRepository.findByEmail(principal.getName()).orElseThrow();
            var wishlistItems = wishlistRepository.getWishlistsByUserId(user.getId());
            if (wishlistItems.isEmpty()) throw new Exception("Your wishlist is empty");
            return wishlistItems;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

    @DeleteMapping(path = "/course/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public MyCustomResponse removeWishlist(@PathVariable(value = "courseId") Integer courseId,
                                           Principal principal) {

        try {
            User myUser = userRepository.findByEmail(principal.getName()).orElseThrow();
            wishlistRepository.deleteWishlistByCourseIdAndUserId(courseId, myUser.getId());
            return new MyCustomResponse("Removed from Wishlist course: " + courseId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
