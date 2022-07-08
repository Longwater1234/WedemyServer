package com.davistiba.wedemyserver.service;

import com.davistiba.wedemyserver.dto.ReviewRequest;
import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.models.Review;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.CourseRepository;
import com.davistiba.wedemyserver.repository.ReviewsRepository;
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
    private ReviewsRepository reviewsRepository;

    /**
     * Insert new review, rating.
     * Calculate and update average rating for course.
     *
     * @param request from frontend
     * @param userId  userId
     */
    @Transactional
    public void handleCourseRating(ReviewRequest request, Integer userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow();
        Review myReview = new Review(request.getRating(), request.getContent(), user, course);
        reviewsRepository.save(myReview);

        //calculate and update average rating for course.
        double avgRating = reviewsRepository.findAverageByCourseId(course.getId());
        course.setRating(BigDecimal.valueOf(avgRating).setScale(2, RoundingMode.DOWN));
        courseRepository.save(course);
    }
}
