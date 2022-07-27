package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.ReviewDTO;
import com.davistiba.wedemyserver.dto.ReviewRequest;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.models.Review;
import com.davistiba.wedemyserver.repository.ReviewRepository;
import com.davistiba.wedemyserver.service.MyUserDetailsService;
import com.davistiba.wedemyserver.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/reviews")
public class ReviewsController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewService reviewService;


    @PostMapping(path = "/")
    @Secured(value = "ROLE_STUDENT")
    public MyCustomResponse addCourseReview(@Valid @RequestBody ReviewRequest review, HttpSession session) {
        try {
            Integer userId = MyUserDetailsService.getSessionUserId(session);
            reviewService.addCourseRating(review, userId);
            return new MyCustomResponse("Thanks for your review!");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not add review", e);
        }
    }

    @PutMapping(path = "/id/{id}")
    @Secured(value = "ROLE_STUDENT")
    public MyCustomResponse editCourseReview(@PathVariable Integer id, @Valid @RequestBody ReviewRequest review) {
        try {
            reviewService.updateCourseRating(id, review);
            return new MyCustomResponse("Thanks for your review!");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not edit review", e);
        }
    }

    @GetMapping(path = "/mine/c/{courseId}")
    @Secured(value = "ROLE_STUDENT")
    public ResponseEntity<Review> getMyReviewOnCourse(@PathVariable Integer courseId, HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        var review = reviewRepository.findByUserIdAndCourseId(userId, courseId).orElse(null);
        return ResponseEntity.ok().body(review);
    }

    @GetMapping(path = "/course/{courseId}")
    public Slice<ReviewDTO> getCourseReviews(@RequestParam(defaultValue = "0") Integer page,
                                             @RequestParam(defaultValue = "createdAt") String sortBy,
                                             @PathVariable Integer courseId) {
        Pageable pageable = PageRequest.of(page, 10, Sort.Direction.DESC, sortBy);
        return reviewRepository.findByCourseId(courseId, pageable);
    }
}
