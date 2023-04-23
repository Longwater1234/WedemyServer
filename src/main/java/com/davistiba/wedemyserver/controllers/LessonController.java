package com.davistiba.wedemyserver.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.davistiba.wedemyserver.models.Lesson;
import com.davistiba.wedemyserver.repository.LessonRepository;

@RestController
@RequestMapping(path = "/lessons")
public class LessonController {

	@Autowired
	private LessonRepository lessonRepository;

	@GetMapping(path = "/course/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Slice<Lesson> getLessonsByCourseId(@PathVariable @NotNull Integer id,
			@RequestParam(defaultValue = "0") Integer page) {
		return lessonRepository.getLessonsByCourseId(id, PageRequest.of(page, 10));
	}

	@GetMapping(path = "/c/{courseId}/eid/{enrollId}")
	@ResponseStatus(HttpStatus.OK)
	@Secured(value = "ROLE_STUDENT")
	public List<Map<String, Object>> getMyWatchedLessons(@PathVariable Integer courseId,
			@PathVariable Integer enrollId) {
		return lessonRepository.getAllMyWatchedLessons(enrollId, courseId);
	}

}
