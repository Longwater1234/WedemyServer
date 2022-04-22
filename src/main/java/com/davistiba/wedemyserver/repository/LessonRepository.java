package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Lesson;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends CrudRepository<Lesson, Integer> {

    @Query(value = "SELECT n FROM Lesson n where n.course.id = ?1 ORDER BY n.position")
    List<Lesson> getLessonsByCourseId(Integer courseId);

}
