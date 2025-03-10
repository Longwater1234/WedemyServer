package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.CategoryDTO;
import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.repository.CourseRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path = "/courses", produces = MediaType.APPLICATION_JSON_VALUE)
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping(path = "/id/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable @NotNull Integer id) {
        return ResponseEntity.of(courseRepository.findById(id));
    }

    @GetMapping(path = "/cat/{category}")
    public List<Course> getCoursesByCategory(@PathVariable @NotBlank String category) {
        var courseList = courseRepository.getCoursesByCategoryEquals(category);
        //FIXME maybe just return empty array?
        if (courseList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No results for given category");
        }
        return courseList;
    }

    @GetMapping(path = "/top")
    public ResponseEntity<List<Course>> getAllTopCourses() {
        var courseList = courseRepository.getTop6CoursesByIsFeatured(true);
        CacheControl cc = CacheControl.maxAge(60, TimeUnit.MINUTES).cachePublic();
        return ResponseEntity.ok().cacheControl(cc).body(courseList);
    }

    @GetMapping(path = "/categories")
    public ResponseEntity<List<CategoryDTO>> getCategoryListDistinct() {
        var categoryDTO = courseRepository.getAllDistinctCategories();
        CacheControl cc = CacheControl.maxAge(60, TimeUnit.MINUTES).cachePublic();
        return ResponseEntity.ok().cacheControl(cc).body(categoryDTO);
    }


    @GetMapping(path = "/search")
    public Slice<Course> searchForCourseByTitle(@RequestParam(defaultValue = "") @NotBlank String title,
                                                @RequestParam(defaultValue = "0") Integer page) {
        if (title.length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Search query too short");
        }
        return courseRepository.getCoursesByTitleContaining(title, PageRequest.of(page, 10));
    }
}
