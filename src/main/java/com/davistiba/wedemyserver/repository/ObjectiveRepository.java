package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.CourseObjective;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectiveRepository extends CrudRepository<CourseObjective, Integer> {

    List<CourseObjective> getCourseObjectivesByCourseId(Integer course_id);

//    List<CourseRepository> findDistinctBy()
}
