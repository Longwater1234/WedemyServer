package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.WishlistCheckResp;
import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.repository.CourseRepository;
import com.davistiba.wedemyserver.repository.WishlistRepository;
import com.davistiba.wedemyserver.service.MyUserDetailsService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@Secured("ROLE_STUDENT")
@RequestMapping(path = "/wishlist", produces = MediaType.APPLICATION_JSON_VALUE)
public class WishlistController {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private CourseRepository courseRepository;


    @PostMapping(path = "/course/{courseId}")
    public ResponseEntity<MyCustomResponse> addNewWishlist(@PathVariable Integer courseId, HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        int count = wishlistRepository.saveByCourseIdAndUserId(courseId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MyCustomResponse(String.format("Added %d item to Wishlist", count)));
    }

    @GetMapping(path = "/status/c/{courseId}")
    public ResponseEntity<WishlistCheckResp> checkIfUserWishlist(@PathVariable @NotNull Integer courseId,
                                                                 HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        boolean inWishlist = wishlistRepository.checkIfExistWishlist(userId, courseId);
        return ResponseEntity.ok(new WishlistCheckResp(inWishlist));
    }

    @GetMapping(path = "/mine")
    public Page<Course> getMyWishlistPaged(@RequestParam(defaultValue = "0") @Min(0) Integer page, HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        return courseRepository.getWishlistByUser(userId, PageRequest.of(Math.abs(page), 5));
    }

    @DeleteMapping(path = "/course/{courseId}")
    public ResponseEntity<MyCustomResponse> removeWishlistByCourseId(@PathVariable @NotNull Integer courseId, HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        int deletedCount = wishlistRepository.deleteByUserIdAndCoursesIn(userId, Collections.singletonList(courseId));
        String message = String.format("Removed %d item from wishlist, courseId %d", deletedCount, courseId);
        return ResponseEntity.ok(new MyCustomResponse(message));
    }

}
