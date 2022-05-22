package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.Lesson;
import com.davistiba.wedemyserver.models.ViewCourseProgress;
import com.davistiba.wedemyserver.repository.EnrollmentRepository;
import com.davistiba.wedemyserver.repository.LessonRepository;
import com.davistiba.wedemyserver.repository.ViewCourseProgressRepository;
import com.davistiba.wedemyserver.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "/enroll")
@Secured(value = {"ROLE_STUDENT"})
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ViewCourseProgressRepository progressRepository;

    @Autowired
    private LessonRepository lessonRepository;


    @GetMapping(path = "/status/c/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Boolean> checkEnrollStatus(@PathVariable @NotNull Integer courseId, HttpSession session) {
        Map<String, Boolean> response = new HashMap<>();
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        boolean isOwned = enrollmentRepository.existsByCourseIdAndUserId(userId, courseId);
        response.put("isOwned", isOwned);
        return response;
    }

    @GetMapping(path = "/progress/summary")
    public List<ViewCourseProgress> getCourseProgress(@NotNull HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        return progressRepository.findTop3ByUserId(userId);
    }


    @GetMapping(path = "/mine")
    public List<ViewCourseProgress> getAllCourseProgress(@NotNull HttpSession session,
                                                         @RequestParam(defaultValue = "0") Integer page) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        return progressRepository.findByUserId(userId, PageRequest.of(page, 10));
    }


    @GetMapping(path = "/resume/course/{courseId}")
    public Map<String, String> getLastViewedVideo(@NotNull HttpSession session, @PathVariable Integer courseId) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        var enrollment = enrollmentRepository.getByUserIdAndCourseId(userId, courseId);
        if (enrollment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't own this course");
        }
        UUID currentLessonId = enrollment.get().getCurrentLesson();
        if (currentLessonId == null) {
            //user has not begun course!
            Lesson firstLesson = lessonRepository.getLessonsByCourseId(courseId, Pageable.ofSize(1)).get(0);
            currentLessonId = firstLesson.getId();
        }
        Map<String, String> response = new HashMap<>();
        response.put("lessonId", currentLessonId.toString());
        return response;

    }

}
