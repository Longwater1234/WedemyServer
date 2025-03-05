package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.ObjectivesDTO;
import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.models.CourseObjective;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.repository.CourseRepository;
import com.davistiba.wedemyserver.repository.ObjectiveRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/objectives", produces = MediaType.APPLICATION_JSON_VALUE)
public class ObjectivesController {

    @Autowired
    private ObjectiveRepository objectiveRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping(value = "/")
    @Secured(value = "ROLE_ADMIN")
    @Transactional
    public ResponseEntity<MyCustomResponse> addNewObjectives(@RequestBody @Valid ObjectivesDTO objDTO) {
        List<String> objectives = objDTO.getObjectives();
        final Course course = courseRepository.findById(objDTO.getCourseId()).orElseThrow();
        List<CourseObjective> coList = objectives.stream().map(o -> new CourseObjective(course, o)).collect(Collectors.toList());
        objectiveRepository.batchInsert(coList, this.jdbcTemplate);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MyCustomResponse("All saved!"));
    }

    @GetMapping(value = "/course/{courseId}")
    public List<CourseObjective> getCourseObjectives(@PathVariable Integer courseId) {
        return objectiveRepository.getCourseObjectivesByCourseId(courseId);
    }
}
