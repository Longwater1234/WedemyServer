package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/courses")
public class CourseController {

    @Autowired
    CourseRepository courseRepository;

    @GetMapping(path = "id/{id}")
    public Course getCoursebyId(@PathVariable(value = "id") @NotNull Integer id) {
        try {
            return courseRepository.findById(id).orElseThrow();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No course of id " + id);
        }
    }

    @GetMapping(path = "category/{category}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Course> getCoursesByCategory(@PathVariable(value = "category")
                                             @NotBlank String category) {
        var courseList = courseRepository.getCoursesByCategoryEquals(category);
        if (courseList.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No results for category " + category);
        return courseList;
    }

    @GetMapping(path = "/all")
    @ResponseStatus(value = HttpStatus.OK)
    public Iterable<Course> getAllCourses() {
        return courseRepository.findAll();
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
