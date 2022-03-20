package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.CategoryDTO;
import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping(path = "/id/{id}")
    public Course getCourseById(@PathVariable(value = "id") @NotNull Integer id) {
        try {
            return courseRepository.findById(id).orElseThrow();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No course of id " + id);
        }
    }

    @GetMapping(path = "/cat/{category}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Course> getCoursesByCategory(@PathVariable(value = "category") @NotBlank String category) {
        var courseList = courseRepository.getCoursesByCategoryEquals(category);
        if (courseList.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No results");

        return courseList;
    }

    @GetMapping(path = "/top")
    @ResponseStatus(value = HttpStatus.OK)
    @Cacheable(value = "courses")
    public List<Course> getAllTopCourses() {
        return courseRepository.getTop6ByRatingGreaterThanEqual(BigDecimal.valueOf(4.5));
    }


    @GetMapping(path = "/categories")
    @ResponseStatus(value = HttpStatus.OK)
    @Cacheable(value = "categories")
    public List<CategoryDTO> getCategoryListDistinct() {
        return courseRepository.getAllDistinctCategories();
    }


    @GetMapping(path = "/search")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Course> searchForCourseByTitle(@RequestParam(value = "title", defaultValue = "") @NotBlank String title) {

        if (title.length() < 4)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Search query too short");
        var searchResults = courseRepository.getCoursesByTitleContaining(title);
        if (searchResults.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No results for your query.");

        return searchResults;
    }
}
