package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.CategoryDTO;
import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path = "/courses", produces = MediaType.APPLICATION_JSON_VALUE)
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping(path = "/id/{id}")
    public Course getCourseById(@PathVariable @NotNull Integer id) {
        try {
            return courseRepository.findById(id).orElseThrow();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No course of id " + id);
        }
    }

    @GetMapping(path = "/cat/{category}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Course> getCoursesByCategory(@PathVariable @NotBlank String category) {
        return courseRepository.getCoursesByCategoryEquals(category);
    }

    @GetMapping(path = "/top")
    @ResponseStatus(value = HttpStatus.OK)
    @Cacheable(value = "courses")
    public List<Course> getAllTopCourses() {
        return courseRepository.getTop6ByRatingGreaterThanEqual(BigDecimal.valueOf(4.5));
    }


    @GetMapping(path = "/categories")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<CategoryDTO>> getCategoryListDistinct() {
        var categoryDTO = courseRepository.getAllDistinctCategories();
        CacheControl cc = CacheControl.maxAge(60, TimeUnit.MINUTES).cachePublic();
        return ResponseEntity.ok().cacheControl(cc).body(categoryDTO);
    }


    @GetMapping(path = "/search")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Course> searchForCourseByTitle(@RequestParam(defaultValue = "") @NotBlank String title) {
        if (title.length() < 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Search query too short");
        }
        return courseRepository.getCoursesByTitleContaining(title);

    }
}
