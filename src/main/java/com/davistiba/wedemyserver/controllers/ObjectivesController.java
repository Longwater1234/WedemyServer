package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.ObjectivesDTO;
import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.models.CourseObjectives;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.repository.CourseRepository;
import com.davistiba.wedemyserver.repository.ObjectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/objectives")
public class ObjectivesController {

    @Autowired
    private ObjectiveRepository objectiveRepository;
    @Autowired
    private CourseRepository courseRepository;

    @PostMapping(value = "/add")
    @Secured(value = "ROLE_ADMIN")
    @Validated
    public ResponseEntity<MyCustomResponse> addNewObjectives(@RequestBody @NotNull ObjectivesDTO objDTO) {
        List<String> objectives = objDTO.getObjectives();

        try {
            Course course = courseRepository.findById(objDTO.getCourseId()).orElseThrow();
            objectives.forEach(o -> objectiveRepository.save(new CourseObjectives(course, o)));
            return ResponseEntity.status(HttpStatus.CREATED).body(new MyCustomResponse("Created"));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(value = "/course/{courseId}")
    public List<CourseObjectives> getCourseObjectives(@PathVariable(value = "courseId") Integer courseId) {
        var objList = objectiveRepository.getCourseObjectivesByCourseId(courseId);
        if (objList.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data!");
        return objList;
    }
}
