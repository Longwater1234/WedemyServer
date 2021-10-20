package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.models.Wishlist;
import com.davistiba.wedemyserver.repository.WishlistRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Secured("ROLE_USER")
@RequestMapping(path = "/wishlist")
@AllArgsConstructor
public class WishlistController {

    private final WishlistRepository wishlistRepository;

    private final Logger logger = LoggerFactory.getLogger(String.valueOf(this));

    @PostMapping(path = "/course/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public MyCustomResponse addNewWishlist(@PathVariable(value = "courseId") Integer courseId,
                                           HttpSession session) {

        try {
            Integer userId = (Integer) session.getAttribute(AuthController.USERID);
            wishlistRepository.saveByCourseIdAndUserId(courseId, userId);
            return new MyCustomResponse("Added to Wishlist: courseId " + courseId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(path = "/mine/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Boolean> checkUserLikedCourse(@PathVariable(value = "courseId") @NotNull Integer courseId,
                                                     HttpSession session) {

        try {
            Map<String, Boolean> response = new HashMap<>();
            Integer userId = (Integer) session.getAttribute(AuthController.USERID);
            boolean isExist = wishlistRepository.checkIfWishlistExists(courseId, userId) == 1;
            response.put("isWishlist", isExist);
            return response;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    @GetMapping(path = "/mine")
    @ResponseStatus(HttpStatus.OK)
    public List<Wishlist> getAllMyWishlistCourses(HttpSession session) {

        Integer userId = (Integer) session.getAttribute(AuthController.USERID);
        var wishlistItems = wishlistRepository.getWishlistsByUserId(userId);
        if (wishlistItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Your wishlist is empty");
        }
        return wishlistItems;
    }

    @DeleteMapping(path = "/course/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public MyCustomResponse removeWishlist(@PathVariable(value = "courseId") Integer courseId,
                                           HttpSession session) {

        try {
            Integer userId = (Integer) session.getAttribute(AuthController.USERID);
            wishlistRepository.deleteByCourseIdAndUserId(courseId, userId);
            return new MyCustomResponse("Removed from Wishlist, courseId " + courseId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
