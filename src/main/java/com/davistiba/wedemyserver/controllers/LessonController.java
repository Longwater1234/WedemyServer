package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.VideoRequest;
import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.models.Lesson;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.repository.CourseRepository;
import com.davistiba.wedemyserver.repository.EnrollmentRepository;
import com.davistiba.wedemyserver.repository.LessonRepository;
import com.davistiba.wedemyserver.service.MyUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "/lessons")
public class LessonController {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    private static final Logger logger = LoggerFactory.getLogger(LessonController.class);


    @GetMapping(path = "/course/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Lesson> getLessonsByCourseId(@PathVariable @NotNull Integer id, @RequestParam(defaultValue = "0") Integer page) {
        return lessonRepository.getLessonsByCourseId(id, PageRequest.of(page, 10));
    }

    @PostMapping(path = "/videolink/builder")
    @Secured(value = "ROLE_USER")
    public ResponseEntity<Lesson> getLessonVideoLink(@NotNull HttpSession session, @RequestBody @Valid VideoRequest request) {
        try {
            Integer userId = MyUserDetailsService.getSessionUserId(session);
            boolean isOwned = enrollmentRepository.existsByCourseIdAndUserId(userId, request.getCourseId());
            if (!isOwned) {
                throw new Exception("You don't own this course");
            }
            UUID lessonId = UUID.fromString(request.getLessonId());
            Integer courseId = request.getCourseId();
            Lesson currentLesson = lessonRepository.findByIdAndCourseId(lessonId, courseId).orElseThrow();
            return ResponseEntity.ok().body(currentLesson);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sorry could not get lesson", e);
        }

    }


    @PostMapping(path = "/")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured(value = "ROLE_ADMIN")
    @Async
    public CompletableFuture<MyCustomResponse> addNewLessons(@RequestBody @NotEmpty List<Lesson> newLessons) {
        //TODO: FOR ADMINS to ADD NEW LESSONS
        long startClock = System.nanoTime();
        final List<Lesson> mamas = new ArrayList<>();
        newLessons.forEach(lesson -> {
            Integer courseId = lesson.getCourse().getId();
            Course course = courseRepository.findById(courseId).orElseThrow();
            lesson.setCourse(course);
            mamas.add(lesson);
        });
        logger.info("totalTime: " + (System.nanoTime() - startClock) / 1e6);
        lessonRepository.saveAll(mamas);
        return CompletableFuture.completedFuture(new MyCustomResponse("All saved!", true));

    }

}
