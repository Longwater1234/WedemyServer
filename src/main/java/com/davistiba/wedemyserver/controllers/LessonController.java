package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.Lesson;
import com.davistiba.wedemyserver.repository.CourseRepository;
import com.davistiba.wedemyserver.repository.LessonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

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
    public Slice<Lesson> getLessonsByCourseId(@PathVariable @NotNull Integer id, @RequestParam(defaultValue = "0") Integer page) {
        return lessonRepository.getLessonsByCourseId(id, PageRequest.of(page, 10));
    }

    @GetMapping(path = "/c/{courseId}/eid/{enrollId}")
    @ResponseStatus(HttpStatus.OK)
    @Secured(value = "ROLE_STUDENT")
    public List<Map<String, Object>> getMyWatchedLessons(@PathVariable Integer courseId, @PathVariable Integer enrollId) {
        return lessonRepository.getAllMyWatchedLessons(enrollId, courseId);
    }


}
