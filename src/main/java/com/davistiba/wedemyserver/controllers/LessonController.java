package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.models.Lesson;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.repository.CourseRepository;
import com.davistiba.wedemyserver.repository.LessonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "/lessons")
public class LessonController {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    private final Logger logger = LoggerFactory.getLogger(String.valueOf(this));


    @GetMapping(path = "/course/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Lesson> getLessonsByCourseId(@PathVariable(name = "id") @NotNull Integer id) {

        var LessonList = lessonRepository.getLessonsByCourse_Id(id);
        if (LessonList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Sorry, no lessons for course id %d", id));
        }
        return LessonList;
    }


    @PostMapping(path = "/")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured(value = "ROLE_ADMIN")
    @Async
    public CompletableFuture<MyCustomResponse> addNewLessons(@RequestBody @NotEmpty List<Lesson> newLessons) {
        try {
            long startClock = System.currentTimeMillis();
            final List<Lesson> mamas = new ArrayList<>();
            newLessons.forEach(lesson -> {
                Integer courseId = lesson.getCourse().getId();
                Course course = courseRepository.findById(courseId).orElseThrow();
                lesson.setCourse(course);
                mamas.add(lesson);
            });
            logger.info("totalTime: " + (System.currentTimeMillis() - startClock));
            lessonRepository.saveAll(mamas);
            return CompletableFuture.completedFuture(new MyCustomResponse("All saved!", true));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

}
