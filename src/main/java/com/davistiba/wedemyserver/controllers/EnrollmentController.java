package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.*;
import com.davistiba.wedemyserver.models.Enrollment;
import com.davistiba.wedemyserver.models.Lesson;
import com.davistiba.wedemyserver.repository.EnrollmentRepository;
import com.davistiba.wedemyserver.repository.LessonRepository;
import com.davistiba.wedemyserver.service.EnrollProgressService;
import com.davistiba.wedemyserver.service.MyUserDetailsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping(path = "/enroll", produces = MediaType.APPLICATION_JSON_VALUE)
@Secured(value = {"ROLE_STUDENT", "ROLE_ADMIN"})
@SecurityRequirement(name = "cookieAuth")
@SecurityRequirement(name = "sessionKey")
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private EnrollProgressService progressService;

    @GetMapping(path = "/status/c/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EnrollStatusDTO> checkEnrollStatus(@PathVariable @NotNull Integer courseId, HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        boolean isOwned = enrollmentRepository.existsByUserIdAndCourseId(userId, courseId);
        return ResponseEntity.ok(new EnrollStatusDTO(isOwned));
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


    @PostMapping(path = "/videolink/builder")
    public ResponseEntity<VideoResponse> getLessonVideoLink(@NotNull HttpSession session,
                                                            @RequestBody @Valid VideoRequest request) {
        try {
            Integer userId = MyUserDetailsService.getSessionUserId(session);
            Optional<Enrollment> enrollment = enrollmentRepository.getOneByUserIdAndCourseId(userId, request.getCourseId());
            if (enrollment.isEmpty()) {
                throw new IllegalArgumentException("You don't own this course");
            }
            UUID lessonId = UUID.fromString(request.getLessonId());
            Lesson currentLesson = lessonRepository.findById(lessonId).orElseThrow();
            VideoResponse response = new VideoResponse(enrollment.get().getId(), currentLesson);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed! Reason: " + e.getMessage(), e);
        }
    }

    @GetMapping(path = "/resume/c/{courseId}")
    public ResponseEntity<ResumeCourseResp> resumeMyCourse(@PathVariable Integer courseId, HttpSession session) {
        Integer userId = MyUserDetailsService.getSessionUserId(session);
        Optional<Enrollment> enrollment = enrollmentRepository.getOneByUserIdAndCourseId(userId, courseId);
        if (enrollment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't own this course");
        }
        Optional<Lesson> nextLesson = progressService.getNextLesson(enrollment.get());
        if (nextLesson.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not get lesson!");
        }
        return ResponseEntity.ok().body(new ResumeCourseResp(String.valueOf(nextLesson.get().getId())));
    }


    @PostMapping(path = "/watched")
    @CacheEvict(value = "student-summary", key = "#session.id")
    public ResponseEntity<WatchStatusResp> updateWatchStatus(@RequestBody @Valid WatchStatusReq status, HttpSession session) {
        try {
            //first, check if user owns course
            Integer userId = MyUserDetailsService.getSessionUserId(session);
            Optional<Enrollment> enrollment = enrollmentRepository.getOneByUserIdAndCourseId(userId, status.getCourseId());
            if (enrollment.isEmpty()) throw new IllegalStateException("You don't own this course");
            // get next Lesson
            WatchStatusResp response = new WatchStatusResp();
            Optional<Lesson> nextLesson = progressService.updateAndGetNextLesson(status, enrollment.get());
            if (nextLesson.isPresent()) {
                response.setNextLessonId(String.valueOf(nextLesson.get().getId()));
            } else {
                response.setNextLessonId(null);
                response.setMessage("Bravo! You have completed the course!");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not update status: " + e.getMessage(), e);
        }

    }

}
