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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "/lessons")
public class LessonController {

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    CourseRepository courseRepository;

    final Logger logger = LoggerFactory.getLogger(String.valueOf(this));

    @GetMapping(path = "/course/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Lesson> getLessonsbyCourseId(@PathVariable(name = "id") @NotBlank Integer id) {
        try {
            var LessonList = lessonRepository.getLessonsByCourse_CourseId(id);
            if (LessonList.isEmpty()) throw new Exception("Lesson of Course Id " + id + "not found");
            return LessonList;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping(path = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Async
    public CompletableFuture<MyCustomResponse> addNewLesson(@RequestBody List<Lesson> newLessons, HttpServletRequest request) {
        try {
            logger.info("Start " + System.currentTimeMillis());
            List<Lesson> mamas = new ArrayList<>();
            for (Lesson lesson : newLessons) {
                Integer courseId = lesson.getCourse().getCourseId();
                Course course = courseRepository.findById(courseId).orElseThrow();
                lesson.setCourse(course);
                mamas.add(lesson);
            }
            logger.info("End " + System.currentTimeMillis());
            lessonRepository.saveAll(mamas);
            return CompletableFuture.completedFuture(new MyCustomResponse("Successful", true));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

}
