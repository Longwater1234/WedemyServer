package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.dto.LessonDTO;
import com.davistiba.wedemyserver.models.Lesson;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LessonRepository extends CrudRepository<Lesson, UUID> {

    @Query(value = "SELECT s FROM Lesson s where s.course.id = ?1 ORDER BY s.position")
    Slice<Lesson> getLessonsByCourseId(int courseId, Pageable pageable);

    @Query("SELECT s FROM Lesson s where s.course.id = ?1 and s.position = ?2")
    Optional<Lesson> findByCourseIdAndPosition(Integer courseId, Integer position);

    @Query("SELECT count(s) from Lesson s where s.course.id = ?1")
    int countByCourseId(int id);

    @Query(value = "SELECT BIN_TO_UUID(s.id) as id, s.lesson_name as lessonName, s.position as `position`, TIME_FORMAT(SEC_TO_TIME(s.length_seconds), " +
            "'%i:%s') AS videoTime, EXISTS(SELECT 1 FROM enroll_progress ep WHERE ep.lesson_id = s.id AND ep.enrollment_id = :enrollId) " +
            "AS isWatched FROM lessons s WHERE s.course_id = :courseId ORDER BY s.position", nativeQuery = true)
    List<LessonDTO> getWatchStatusListByEnrollment(long enrollId, int courseId);

    @Query(value = "SELECT s FROM Lesson s LEFT JOIN EnrollProgress ep ON s.id = ep.lesson.id " +
            "AND ep.enrollment.id = :enrollmentId WHERE ep.lesson.id IS NULL AND s.course.id = :courseId " +
            "ORDER BY s.position")
    List<Lesson> getAllNotWatchedByEnrollmentId(long enrollmentId, int courseId, Pageable pageable);

    //just the first one
    default Optional<Lesson> getFirstNotWatchedInEnrollment(long enrollmentId, int courseId) {
        List<Lesson> lessonList = getAllNotWatchedByEnrollmentId(enrollmentId, courseId, PageRequest.ofSize(1));
        return lessonList.isEmpty() ? Optional.empty() : Optional.of(lessonList.get(0));
    }
}
