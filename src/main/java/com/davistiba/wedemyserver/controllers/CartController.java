package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.fakes.CartBill;
import com.davistiba.wedemyserver.fakes.CartCount;
import com.davistiba.wedemyserver.fakes.ItemCartStatus;
import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.repository.CartRepository;
import com.davistiba.wedemyserver.repository.CourseRepository;
import com.davistiba.wedemyserver.service.MyUserDetailsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

@RestController
@Secured(value = "ROLE_STUDENT")
@RequestMapping(path = "/cart", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "cookieAuth")
@SecurityRequirement(name = "sessionKey")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping(path = "/course/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
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
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ItemCartStatus> checkUserCartItem(@PathVariable @NotNull Integer courseId, HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        boolean inCart = cartRepository.checkIfCourseInCart(userId, courseId) > 0;
        Map<String, Boolean> response = Collections.singletonMap("inCart", inCart);
        return ResponseEntity.ok(new ItemCartStatus());
    }


    @GetMapping(path = "/mine")
    @ResponseStatus(HttpStatus.OK)
    public Page<Course> getAllMyCartItems(@RequestParam(defaultValue = "0") Integer page, HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        return courseRepository.getCartListByUser(userId, PageRequest.of(Math.abs(page), 5));
    }

    @GetMapping(path = "/mine/bill")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CartBill> getMyCartBill(HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        BigDecimal totalPrice = cartRepository.getTotalBillForUser(userId);
        return ResponseEntity.ok(new CartBill());
    }

    @GetMapping(path = "/mine/count")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CartCount> countMyCartItems(HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        long cartCount = cartRepository.countCartByUserIdEquals(userId);
        Map<String, Long> response = Collections.singletonMap("cartCount", cartCount);
        return ResponseEntity.ok(new CartCount());
    }

    @DeleteMapping(path = "/course/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MyCustomResponse> removeCartByCourseId(@PathVariable @NotNull Integer courseId,
                                                                 HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        int deletedCount = cartRepository.deleteByUserIdAndCoursesIn(userId, Collections.singleton(courseId));
        if (deletedCount != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not remove from cart");
        }
        return ResponseEntity.ok().body(new MyCustomResponse("Removed from Cart, course " + courseId));
    }


}
