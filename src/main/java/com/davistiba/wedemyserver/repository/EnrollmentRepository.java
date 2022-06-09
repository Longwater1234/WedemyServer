package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.dto.EnrollmentDTO;
import com.davistiba.wedemyserver.models.Enrollment;
import com.davistiba.wedemyserver.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    @Query("SELECT (COUNT(e) > 0) FROM Enrollment e WHERE e.user.id = ?1 AND e.course.id = ?2")
    boolean existsByUserIdAndCourseId(Integer userId, Integer courseId);

    long countEnrollmentByUserAndIsCompleted(User user, Boolean isCompleted);

    long countEnrollmentByUser(User user);

    @Query(value = "SELECT e FROM Enrollment e WHERE e.user.id = ?1 AND e.course.id = ?2")
    Optional<Enrollment> getByUserIdAndCourseId(Integer userId, Integer courseId);

    @Query("SELECT new com.davistiba.wedemyserver.dto.EnrollmentDTO(e.id, e.progress, c.title, c.thumbUrl, c.id) FROM Enrollment e " +
            "INNER JOIN Course c ON e.course.id = c.id WHERE e.user.id = ?1 ORDER BY e.id DESC")
    List<EnrollmentDTO> findByUserId(Integer userId, Pageable pageable);

    @Modifying
    @Query("UPDATE Enrollment e SET e.currentLessonId = ?1, e.progress = ?2, e.updatedAt = ?3 WHERE e.id = ?4")
    int updateCustom(UUID currentLessonId, BigDecimal progress, Instant updatedAt, Integer id);
}
