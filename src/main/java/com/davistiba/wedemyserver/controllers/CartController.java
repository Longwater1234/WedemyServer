package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.Cart;
import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.CartRepository;
import com.davistiba.wedemyserver.repository.CourseRepository;
import com.davistiba.wedemyserver.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Secured(value = "ROLE_USER")
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
            User user = MyUserDetailsService.getSessionUserDetails(session); //from redis store
            Course course = courseRepository.findById(courseId).orElseThrow(); // verify if exists
            cartRepository.save(new Cart(course, user));
            return new MyCustomResponse(String.format("Added item to Cart, course %d", courseId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not add to cart", e);
        }

    }

    @GetMapping(path = "/status/c/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Boolean> checkUserCartItem(@PathVariable @NotNull Integer courseId, HttpSession session) {

        Map<String, Boolean> response = new HashMap<>();
        Integer userId = (Integer) session.getAttribute(MyUserDetailsService.USERID);
        boolean isExist = cartRepository.checkIfCourseInCart(userId, courseId);
        response.put("inCart", isExist);
        return response;
    }


    @GetMapping(path = "/mine")
    @ResponseStatus(HttpStatus.OK)
    public List<Course> getAllMyCartItems(@RequestParam(defaultValue = "0") Integer page, HttpSession session) {
        Integer userId = (Integer) session.getAttribute(MyUserDetailsService.USERID);
        return courseRepository.getCoursesCartByUser(userId, PageRequest.of(page, 10));
    }

    @GetMapping(path = "/mine/count")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Integer> countMyCartItems(HttpSession session) {

        Integer userId = (Integer) session.getAttribute(MyUserDetailsService.USERID);
        Map<String, Integer> response = new HashMap<>();
        int cartCount = cartRepository.countCartByUserIdEquals(userId);
        response.put("cartCount", cartCount);
        return response;
    }

    @DeleteMapping(path = "/course/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public MyCustomResponse removeCartByCourseId(@PathVariable @NotNull Integer courseId, HttpSession session) {

        Integer userId = (Integer) session.getAttribute(MyUserDetailsService.USERID);
        int ok = cartRepository.deleteAllByUserIdAndCoursesIn(userId, Collections.singleton(courseId));
        if (ok != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not remove from cart");
        }
        return new MyCustomResponse("Removed from Cart, course " + courseId);
    }

    @DeleteMapping(path = "/id/{cartId}")
    @ResponseStatus(HttpStatus.OK)
    public MyCustomResponse removeCartById(@PathVariable @NotNull Integer cartId) {
        try {
            cartRepository.deleteById(cartId);
            return new MyCustomResponse("Removed from Wishlist, id " + cartId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


}
