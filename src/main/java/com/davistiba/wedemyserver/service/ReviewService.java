package com.davistiba.wedemyserver.service;

import com.davistiba.wedemyserver.dto.ReviewRequest;
import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.models.Review;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.CourseRepository;
import com.davistiba.wedemyserver.repository.ReviewRepository;
import com.davistiba.wedemyserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ReviewService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    /**
     * Insert new review.
     * Then, re-calculate and update AVG rating for course.
     * All in single DB transaction
     *
     * @param request from frontend
     * @param userId  userId
     */
    @Transactional
    public void addCourseRating(ReviewRequest request, Integer userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow();
        Review myReview = new Review(request.getRating(), request.getContent(), user, course);
        reviewRepository.save(myReview);

        //calculate and update average rating for course.
        double avgRating = reviewRepository.getAverageByCourseId(course.getId());
        course.setRating(BigDecimal.valueOf(avgRating).setScale(2, RoundingMode.FLOOR));
        courseRepository.save(course);
    }

    /**
     * UPDATE existing review, and modify AVG course rating
     */
    @Transactional
    public void updateCourseRating(Integer reviewId, ReviewRequest request) {
        Review myReview = reviewRepository.findById(reviewId).orElseThrow();
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow();
        myReview.setRating(request.getRating());
        myReview.setContent(request.getContent());
        reviewRepository.save(myReview);

        //calculate and update average rating for course.
        double avgRating = reviewRepository.getAverageByCourseId(course.getId());
        course.setRating(BigDecimal.valueOf(avgRating).setScale(2, RoundingMode.FLOOR));
        courseRepository.save(course);
    }
}
