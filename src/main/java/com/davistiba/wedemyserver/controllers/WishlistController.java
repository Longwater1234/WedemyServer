package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.CourseRepository;
import com.davistiba.wedemyserver.repository.WishlistRepository;
import com.davistiba.wedemyserver.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
@Secured("ROLE_STUDENT")
@RequestMapping(path = "/wishlist")
public class WishlistController {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private CourseRepository courseRepository;


    @PostMapping(path = "/course/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public MyCustomResponse addNewWishlist(@PathVariable Integer courseId, HttpSession session) {

        Integer userId = MyUserDetailsService.getSessionUserId(session);
        int count = wishlistRepository.saveByCourseIdAndUserId(courseId, userId);
        return new MyCustomResponse(String.format("Added %d to Wishlist", count));
    }

    @GetMapping(path = "/status/c/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Boolean> checkUserLikedCourse(@PathVariable @NotNull Integer courseId, HttpSession session) {

        Map<String, Boolean> response = new HashMap<>();
        Integer userId = (Integer) session.getAttribute(MyUserDetailsService.USERID);
        boolean isExist = wishlistRepository.checkIfCourseInWishlist(userId, courseId);
        response.put("inWishlist", isExist);
        return response;
    }


    @GetMapping(path = "/mine")
    @ResponseStatus(HttpStatus.OK)
    public List<Course> getAllMyWishlistCourses(@RequestParam(defaultValue = "0") Integer page, HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        return courseRepository.getCoursesWishlistByUser(userId, PageRequest.of(page, 10));
    }

    @DeleteMapping(path = "/course/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public MyCustomResponse removeWishlistByCourseId(@PathVariable @NotNull Integer courseId, HttpSession session) {

        Integer userId = MyUserDetailsService.getSessionUserId(session);
        int ok = wishlistRepository.deleteByCourseIdAndUserId(courseId, userId);
        if (ok != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not remove from wishlist");
        }
        return new MyCustomResponse("Removed from Wishlist, course " + courseId);
    }


    @DeleteMapping(path = "/id/{wishlistId}")
    @ResponseStatus(HttpStatus.OK)
    public MyCustomResponse removeWishlistById(HttpSession session, @PathVariable @NotNull Integer wishlistId) {
        try {
            User user = MyUserDetailsService.getSessionUserInfo(session); //from redis Store
            wishlistRepository.deleteByIdAndUser(wishlistId, user);
            return new MyCustomResponse("Removed from Wishlist, id " + wishlistId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not remove from wishlist", e);
        }
    }
}
