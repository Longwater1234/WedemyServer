package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Lesson;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LessonRepository extends CrudRepository<Lesson, UUID> {

    @Query(value = "SELECT s FROM Lesson s where s.course.id = ?1 ORDER BY s.position")
    Slice<Lesson> getLessonsByCourseId(Integer courseId, Pageable pageable);

    @Query("SELECT s FROM Lesson s where s.course.id = ?1 and s.position = ?2")
    Optional<Lesson> findByCourseIdAndPosition(Integer courseId, Integer position);

    @Query("SELECT count(s) from Lesson s where s.course.id = ?1")
    long countByCourseId(Integer id);

    @Query(value = "SELECT BIN_TO_UUID(s.id) as id, s.lesson_name, s.position, TIME_FORMAT(SEC_TO_TIME(s.length_seconds), " +
            "'%i:%s') AS video_time, EXISTS(SELECT 1 FROM enroll_progress ep WHERE ep.lesson_id = s.id AND ep.enrollment_id = ?1) " +
            "AS is_watched FROM lessons s WHERE s.course_id = ?2 ORDER BY s.position", nativeQuery = true)
    List<Map<String, Object>> getWatchStatusListByEnrollment(Long enrollId, Integer courseId);

    @Query(value = "SELECT s FROM Lesson s LEFT JOIN EnrollProgress ep ON s.id = ep.lesson.id " +
            "AND ep.enrollment.id = :enrollmentId WHERE ep.lesson.id IS NULL AND s.course.id = :courseId " +
            "ORDER BY s.position")
    List<Lesson> getAllNotWatchedByEnrollmentId(Long enrollmentId, Integer courseId, Pageable pageable);

    //just the first one
    default Optional<Lesson> getFirstNotWatchedByCourseId(Long enrollmentId, Integer courseId) {
        var lessonList = getAllNotWatchedByEnrollmentId(enrollmentId, courseId, PageRequest.ofSize(1));
        return lessonList.isEmpty() ? Optional.empty() : Optional.of(lessonList.get(0));
    }
}
