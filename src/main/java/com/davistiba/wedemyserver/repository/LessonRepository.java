package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Lesson;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LessonRepository extends CrudRepository<Lesson, Integer> {

    @Query(value = "SELECT n FROM Lesson n where n.course.id = ?1 ORDER BY n.position")
    List<Lesson> getLessonsByCourseId(Integer courseId, Pageable pageable);

    @Query("select l from Lesson l where l.id = ?1 and l.course.id = ?2")
    Optional<Lesson> findByIdAndCourseId(UUID lessonId, Integer courseId);

    @Query("select l from Lesson l where l.course.id = ?1 and l.position = ?2")
    Optional<Lesson> findByCourseIdAndPosition(Integer courseId, Integer position);

    @Query("select count(l) from Lesson l where l.course.id = ?1")
    long countByCourseId(Integer id);


}
