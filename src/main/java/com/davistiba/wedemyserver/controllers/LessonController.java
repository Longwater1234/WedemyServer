package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.LessonDTO;
import com.davistiba.wedemyserver.models.Lesson;
import com.davistiba.wedemyserver.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/lessons", produces = MediaType.APPLICATION_JSON_VALUE)
public class LessonController {

    @Autowired
    private LessonRepository lessonRepository;

    @GetMapping(path = "/course/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Slice<Lesson> getLessonsByCourseId(@PathVariable @NotNull Integer id,
                                              @RequestParam(defaultValue = "0") @Min(0) Integer page) {
        return lessonRepository.getLessonsByCourseId(id, PageRequest.of(page, 10));
    }

    @GetMapping(path = "/c/{courseId}/e/{enrollId}")
    @ResponseStatus(HttpStatus.OK)
    @Secured(value = "ROLE_STUDENT")
    public List<LessonDTO> getAllMyLessonsInEnrollment(@PathVariable Integer courseId,
                                                       @PathVariable Long enrollId) {
        return lessonRepository.getWatchStatusListByEnrollment(enrollId, courseId);
    }

}
