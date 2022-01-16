package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.Cart;
import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.repository.CartRepository;
import com.davistiba.wedemyserver.repository.CourseRepository;
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

        Integer userId = (Integer) session.getAttribute(AuthController.USERID);
        try {
            Course course = courseRepository.findById(courseId).orElseThrow(); // verify if exists
            int k = cartRepository.addToCartCustom(course.getId(), userId, course.getPrice());
            return new MyCustomResponse(String.format("Added %d item to Cart, courseId %d", k, courseId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            //TODO remove e.getMessage in Production
        }
    }

    @GetMapping(path = "/mine/course/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Boolean> checkUserCartItem(@PathVariable(value = "courseId") @NotNull Integer courseId,
                                                  HttpSession session) {

        Map<String, Boolean> response = new HashMap<>();
        Integer userId = (Integer) session.getAttribute(AuthController.USERID);
        boolean isExist = cartRepository.checkIfCartItemExists(courseId, userId) == 1;
        response.put("inCart", isExist);
        return response;

    }


    @GetMapping(path = "/mine")
    @ResponseStatus(HttpStatus.OK)
    public List<Cart> getAllMyCartItems(HttpSession session) {

        Integer userId = (Integer) session.getAttribute(AuthController.USERID);
        var cartItems = cartRepository.getCartByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Your cart is empty");
        }
        return cartItems;
    }

    @GetMapping(path = "/mine/count")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Integer> countMyCartItems(HttpSession session) {

        Integer userId = (Integer) session.getAttribute(AuthController.USERID);
        Map<String, Integer> response = new HashMap<>();
        int cartCount = cartRepository.countCartByUserIdEquals(userId);
        response.put("cartCount", cartCount);
        return response;
    }

    @DeleteMapping(path = "/course/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public MyCustomResponse removeCartByCourseId(@PathVariable(value = "courseId") @NotNull Integer courseId,
                                                 HttpSession session) {

        Integer userId = (Integer) session.getAttribute(AuthController.USERID);
        int ok = cartRepository.deleteByCourseIdAndUserId(courseId, userId);
        if (ok != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not remove from cart");
        }
        return new MyCustomResponse("Removed from Cart, courseId " + courseId);
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
