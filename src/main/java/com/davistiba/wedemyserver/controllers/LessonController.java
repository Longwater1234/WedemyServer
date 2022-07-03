package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.models.Lesson;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.repository.CourseRepository;
import com.davistiba.wedemyserver.repository.LessonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "/lessons")
public class LessonController {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    private static final Logger logger = LoggerFactory.getLogger(LessonController.class);

    @GetMapping(path = "/course/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Lesson> getLessonsByCourseId(@PathVariable @NotNull Integer id, @RequestParam(defaultValue = "0") Integer page) {
        return lessonRepository.getLessonsByCourseId(id, PageRequest.of(page, 10));
    }

    @PostMapping(path = "/")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured(value = "ROLE_ADMIN")
    @Async
    public CompletableFuture<MyCustomResponse> addNewLessons(@RequestBody @NotEmpty List<Lesson> newLessons) {
        long startClock = System.nanoTime();
        final List<Lesson> mamas = new ArrayList<>();
        newLessons.forEach(lesson -> {
            Integer courseId = lesson.getCourse().getId();
            Course course = courseRepository.findById(courseId).orElseThrow();
            lesson.setCourse(course);
            mamas.add(lesson);
        });
        lessonRepository.saveAll(mamas);
        logger.info("totalTime: {} ms", (System.nanoTime() - startClock) / 1e6);
        return CompletableFuture.completedFuture(new MyCustomResponse("All saved!", true));
    }


    @GetMapping(path = "/c/{courseId}/enroll/{enrollId}")
    @ResponseStatus(HttpStatus.OK)
    @Secured(value = "ROLE_STUDENT")
    public List<Map<String, Object>> getMyWatchedLessons(@PathVariable Integer courseId, @PathVariable Integer enrollId) {
        return lessonRepository.getAllWatchedLessons(enrollId, courseId);
    }


}
