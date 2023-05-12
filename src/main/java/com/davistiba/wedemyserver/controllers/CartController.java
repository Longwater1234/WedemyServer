package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.repository.CartRepository;
import com.davistiba.wedemyserver.repository.CourseRepository;
import com.davistiba.wedemyserver.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;

@RestController
@Secured(value = "ROLE_STUDENT")
@RequestMapping(path = "/cart")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CourseRepository courseRepository;


    @PostMapping(path = "/course/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public MyCustomResponse addSingleItem(HttpSession session, @PathVariable Integer courseId) {
        try {
            Integer userId = MyUserDetailsService.getSessionUserId(session);
            Course course = courseRepository.findById(courseId).orElseThrow();
            int count = cartRepository.addToCartCustom(course.getId(), userId, course.getPrice());
            return new MyCustomResponse(String.format("Added %d item to Cart", count));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not add to cart", e);
        }

    }

    @GetMapping(path = "/status/c/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Boolean> checkUserCartItem(@PathVariable @NotNull Integer courseId, HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        boolean inCart = cartRepository.checkIfCourseInCart(userId, courseId) > 0;
        Map<String, Boolean> response = Collections.singletonMap("inCart", inCart);
        return response;
    }


    @GetMapping(path = "/mine")
    @ResponseStatus(HttpStatus.OK)
    public Slice<Course> getAllMyCartItems(@RequestParam(defaultValue = "0") Integer page, HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        return courseRepository.getCoursesCartByUser(userId, PageRequest.of(Math.abs(page), 10));
    }

    @GetMapping(path = "/mine/count")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Long> countMyCartItems(HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        long cartCount = cartRepository.countCartByUserIdEquals(userId);
        Map<String, Long> response = Collections.singletonMap("cartCount", cartCount);
        return response;
    }

    @DeleteMapping(path = "/course/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public MyCustomResponse removeCartByCourseId(@PathVariable @NotNull Integer courseId, HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        int deletedCount = cartRepository.deleteByUserIdAndCoursesIn(userId, Collections.singleton(courseId));
        if (deletedCount != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not remove from cart");
        }
        return new MyCustomResponse("Removed from Cart, course " + courseId);
    }


}
