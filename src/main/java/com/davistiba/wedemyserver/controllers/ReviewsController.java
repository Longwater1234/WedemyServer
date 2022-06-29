package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.ReviewDTO;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.repository.CourseRepository;
import com.davistiba.wedemyserver.repository.ReviewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/reviews")
public class ReviewsController {

    @Autowired
    private ReviewsRepository reviewsRepository;

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping(path = "/")
    @Secured(value = "ROLE_STUDENT")
    public MyCustomResponse addCourseReview(@Valid ReviewDTO review, @NotNull HttpSession session) {
        //TODO add method to add review here
        return new MyCustomResponse("Successfully added review for course");
    }

    @GetMapping(path = "/course/{courseId}")
    public Page<ReviewDTO> getCourseReviews(@RequestParam(defaultValue = "0") Integer page, @PathVariable Integer courseId) {
        return reviewsRepository.findByCourseIdOrderByCreatedAtDesc(courseId, PageRequest.of(page, 10));
    }
}
