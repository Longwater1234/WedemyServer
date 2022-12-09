package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.CourseObjective;
import com.davistiba.wedemyserver.repository.ObjectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = "/objectives", produces = MediaType.APPLICATION_JSON_VALUE)
public class ObjectivesController {

    @Autowired
    private ObjectiveRepository objectiveRepository;

    @GetMapping(value = "/course/{courseId}")
    public List<CourseObjective> getCourseObjectives(@PathVariable Integer courseId) {
        var objList = objectiveRepository.getCourseObjectivesByCourseId(courseId);
        if (objList.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data!");
        return objList;
    }
}
