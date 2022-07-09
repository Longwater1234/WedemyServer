package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.ReviewDTO;
import com.davistiba.wedemyserver.dto.ReviewRequest;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.models.Review;
import com.davistiba.wedemyserver.repository.ReviewsRepository;
import com.davistiba.wedemyserver.service.MyUserDetailsService;
import com.davistiba.wedemyserver.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/reviews")
public class ReviewsController {

    @Autowired
    private ReviewsRepository reviewsRepository;

    @Autowired
    private ReviewService reviewService;


    @PostMapping(path = "/")
    @Secured(value = "ROLE_STUDENT")
    public MyCustomResponse addCourseReview(@Valid @RequestBody ReviewRequest review, @NotNull HttpSession session) {
        try {
            Integer userId = MyUserDetailsService.getSessionUserId(session);
            reviewService.updateCourseRating(review, userId);
            return new MyCustomResponse("Thanks for your review!");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not add review", e);
        }
    }

    @GetMapping(path = "/mine/c/{courseId}")
    public Review getMyReviewOnCourse(@PathVariable Integer courseId, @NotNull HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        return reviewsRepository.findByUserIdAndCourseId(userId, courseId).orElse(null);
    }

    @GetMapping(path = "/course/{courseId}")
    public Slice<ReviewDTO> getCourseReviews(@RequestParam(defaultValue = "0") Integer page,
                                             @RequestParam(defaultValue = "createdAt") String sortBy,
                                             @PathVariable Integer courseId) {
        Pageable pageable = PageRequest.of(page, 10, Sort.Direction.DESC, sortBy);
        return reviewsRepository.findByCourseId(courseId, pageable);
    }
}
