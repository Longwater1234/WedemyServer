package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.repository.CourseRepository;
import com.davistiba.wedemyserver.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class WishlistController {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private CourseRepository courseRepository;


    @PostMapping(path = "/course/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public MyCustomResponse addNewWishlist(@PathVariable Integer courseId, HttpSession session) {

        Integer userId = (Integer) session.getAttribute(AuthController.USERID);
        try {
            Course course = courseRepository.findById(courseId).orElseThrow(); // verify if exists
            int w = wishlistRepository.saveByCourseIdAndUserId(course.getId(), userId);
            return new MyCustomResponse(String.format("Added %d item to Wishlist, courseId %d", w, courseId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not add to wishlist", e);
        }

    }

    @GetMapping(path = "/mine/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Boolean> checkUserLikedCourse(@PathVariable @NotNull Integer courseId, HttpSession session) {

        Map<String, Boolean> response = new HashMap<>();
        Integer userId = (Integer) session.getAttribute(AuthController.USERID);
        boolean isExist = wishlistRepository.checkIfWishlistExists(courseId, userId) == 1;
        response.put("inWishlist", isExist);
        return response;

    }


    @GetMapping(path = "/mine")
    @ResponseStatus(HttpStatus.OK)
    public List<Course> getAllMyWishlistCourses(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(AuthController.USERID);
        return courseRepository.getCoursesWishlistByUser(userId);
    }

    @DeleteMapping(path = "/course/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public MyCustomResponse removeWishlistByCourseId(@PathVariable @NotNull Integer courseId, HttpSession session) {

        Integer userId = (Integer) session.getAttribute(AuthController.USERID);
        int ok = wishlistRepository.deleteByCourseIdAndUserId(courseId, userId);
        if (ok != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not remove wishlist");
        }
        return new MyCustomResponse("Removed from Wishlist, courseId " + courseId);
    }

    @DeleteMapping(path = "/id/{wishlistId}")
    @ResponseStatus(HttpStatus.OK)
    public MyCustomResponse removeWishlistById(@PathVariable @NotNull Integer wishlistId) {
        try {
            wishlistRepository.deleteById(wishlistId);
            return new MyCustomResponse("Removed from Wishlist, id " + wishlistId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not remove wishlist");
        }
    }
}
