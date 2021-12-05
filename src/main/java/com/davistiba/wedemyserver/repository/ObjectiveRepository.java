package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.CourseObjectives;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ObjectiveRepository extends CrudRepository<CourseObjectives, Integer> {

    List<CourseObjectives> getCourseObjectivesByCourseId(Integer course_id);

}
