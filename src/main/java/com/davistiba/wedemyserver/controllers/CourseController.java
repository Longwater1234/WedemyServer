package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping(path = "/courses")
public class CourseController {

    @Autowired
    CourseRepository courseRepository;

    @GetMapping(path = "category/{category}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Course> getCoursesByCategory(@PathVariable(value = "category")
                                             @NotBlank String category) {
        var courseList = courseRepository.getCoursesByCategoryEquals(category);
        if (courseList.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No results for category " + category);
        return courseList;
    }

    @GetMapping(path = "/top")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Course> getTopCourses() {
        return courseRepository.getTopByRatingGreaterThanEqual(4.5);
    }

    @GetMapping(path = "/search")
    public List<Course> searchForCourse(@RequestParam(value = "title", defaultValue = "")
                                        @NotBlank String title) {

        if (title.length() < 4) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Query too short");
        var searchResults = courseRepository.getCoursesByTitleIsLike(title);
        if (searchResults.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No results for " + title);
        }
        return searchResults;
    }
}
