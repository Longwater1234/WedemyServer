package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.CourseObjective;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ObjectiveRepository extends CrudRepository<CourseObjective, Integer> {

    List<CourseObjective> getCourseObjectivesByCourseId(Integer course_id);

}
