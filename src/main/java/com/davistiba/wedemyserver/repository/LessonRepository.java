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

    @Query("SELECT s FROM Lesson s WHERE s.id = ?1 and s.course.id = ?2")
    Optional<Lesson> findByIdAndCourseId(UUID lessonId, Integer courseId);

    @Query("SELECT s FROM Lesson s where s.course.id = ?1 and s.position = ?2")
    Optional<Lesson> findByCourseIdAndPosition(Integer courseId, Integer position);

    @Query("SELECT count(s) from Lesson s where s.course.id = ?1")
    long countByCourseId(Integer id);

    Optional<Lesson> findLessonById(UUID id);


}
