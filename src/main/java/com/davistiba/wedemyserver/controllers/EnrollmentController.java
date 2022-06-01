package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.EnrollmentDTO;
import com.davistiba.wedemyserver.dto.WatchStatus;
import com.davistiba.wedemyserver.models.Lesson;
import com.davistiba.wedemyserver.repository.EnrollmentRepository;
import com.davistiba.wedemyserver.repository.LessonRepository;
import com.davistiba.wedemyserver.service.EnrollProgressService;
import com.davistiba.wedemyserver.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "/enroll")
@Secured(value = {"ROLE_STUDENT", "ROLE_ADMIN"})
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private EnrollProgressService progressService;

    @GetMapping(path = "/status/c/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Boolean> checkEnrollStatus(@PathVariable @NotNull Integer courseId, HttpSession session) {
        Map<String, Boolean> response = new HashMap<>();
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        boolean isOwned = enrollmentRepository.existsByUserIdAndCourseId(userId, courseId);
        response.put("isOwned", isOwned);
        return response;
    }

    @GetMapping(path = "/progress/summary")
    public List<EnrollmentDTO> getCourseProgress(@NotNull HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        return enrollmentRepository.findByUserId(userId, Pageable.ofSize(3));
    }


    @GetMapping(path = "/mine")
    public List<EnrollmentDTO> getAllCourseProgress(@NotNull HttpSession session,
                                                    @RequestParam(defaultValue = "0") Integer page) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        return enrollmentRepository.findByUserId(userId, PageRequest.of(page, 10));
    }


    @GetMapping(path = "/resume/course/{courseId}")
    @Secured(value = "ROLE_STUDENT")
    public Map<String, String> getLastViewedVideo(@NotNull HttpSession session, @PathVariable Integer courseId) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        var enrollment = enrollmentRepository.getByUserIdAndCourseId(userId, courseId);
        if (enrollment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't own this course");
        }
        UUID currentLessonId = enrollment.get().getCurrentLessonId();
        if (currentLessonId == null) {
            //fresh course for User!
            Lesson firstLesson = lessonRepository.findByCourseIdAndPosition(courseId, 1).orElseThrow();
            currentLessonId = firstLesson.getId();
        }
        Map<String, String> response = new HashMap<>();
        response.put("lessonId", currentLessonId.toString());
        return response;
    }

    @PostMapping(path = "/watched")
    @Secured(value = "ROLE_STUDENT")
    public Map<String, String> updateWatchStatus(@NotNull HttpSession session, @RequestBody @Valid WatchStatus status) {
        try {
            //first, check if user owns course
            Integer userId = MyUserDetailsService.getSessionUserId(session);
            boolean isOwned = enrollmentRepository.existsByUserIdAndCourseId(userId, status.getCourseId());
            if (!isOwned) throw new Exception("You don't own this course");
            //get next Lesson
            Lesson nextLesson = progressService.updateAndGetNextLesson(status);
            Map<String, String> response = new HashMap<>();
            response.put("nextLessonId", nextLesson.getId().toString());
            return response;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not update status: " + e.getMessage(), e);
        }

    }

}
