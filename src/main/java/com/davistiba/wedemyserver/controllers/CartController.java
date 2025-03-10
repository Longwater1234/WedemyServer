package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.repository.CartRepository;
import com.davistiba.wedemyserver.repository.CourseRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

@RestController
@Secured(value = "ROLE_STUDENT")
@RequestMapping(path = "/cart", produces = MediaType.APPLICATION_JSON_VALUE)
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping(path = "/course/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<MyCustomResponse> addSingleItem(HttpSession session, @PathVariable Integer courseId) {
        try {
            Integer userId = MyUserDetailsService.getSessionUserId(session);
            Course course = courseRepository.findById(courseId).orElseThrow();
            int count = cartRepository.addToCartCustom(course.getId(), userId, course.getPrice());
            return ResponseEntity.ok(new MyCustomResponse(String.format("Added %d item to Cart", count)));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not add to cart", e);
        }

    }

    @GetMapping(path = "/status/c/{courseId}")
    public Map<String, Boolean> checkUserCartItem(@PathVariable @NotNull Integer courseId, HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        boolean inCart = cartRepository.checkIfCourseInCart(userId, courseId) > 0;
        Map<String, Boolean> response = Collections.singletonMap("inCart", inCart);
        return response;
    }


    @GetMapping(path = "/mine")
    @Validated
    public Page<Course> getAllMyCartItems(@RequestParam(defaultValue = "0") @Min(0) Integer page,
                                          HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        return courseRepository.getCartListByUser(userId, PageRequest.of(Math.abs(page), 5));
    }

    @GetMapping(path = "/mine/bill")
    public Map<String, BigDecimal> getMyCartBill(HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        BigDecimal totalPrice = cartRepository.getTotalBillForUser(userId);
        return Collections.singletonMap("totalPrice", totalPrice);
    }

    @GetMapping(path = "/mine/count")
    public Map<String, Long> countMyCartItems(HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        long cartCount = cartRepository.countCartByUserIdEquals(userId);
        Map<String, Long> response = Collections.singletonMap("cartCount", cartCount);
        return response;
    }

    @DeleteMapping(path = "/course/{courseId}")
    public ResponseEntity<MyCustomResponse> removeCartByCourseId(@PathVariable @NotNull Integer courseId,
                                                                 HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        int deletedCount = cartRepository.deleteByUserIdAndCoursesIn(userId, Collections.singleton(courseId));
        if (deletedCount != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not remove from cart");
        }
        return ResponseEntity.ok(new MyCustomResponse("Removed from Cart, course " + courseId));
    }


}
