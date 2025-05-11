package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.dto.EnrollmentDTO;
import com.davistiba.wedemyserver.models.Enrollment;
import com.davistiba.wedemyserver.models.User;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends CrudRepository<Enrollment, Long> {
    @Query("SELECT EXISTS (SELECT 1 FROM Enrollment e WHERE e.user.id = ?1 AND e.course.id = ?2)")
    boolean existsByUserIdAndCourseId(Integer userId, Integer courseId);

    long countEnrollmentByUserAndIsCompleted(User user, Boolean isCompleted);

    long countEnrollmentByUser(User user);

    @Query("SELECT new com.davistiba.wedemyserver.dto.EnrollmentDTO(e.id, e.progress, c.title, c.thumbUrl, c.id) FROM Enrollment e " +
           "INNER JOIN Course c ON e.course.id = c.id WHERE e.user.id = ?1 ORDER BY e.id DESC")
    List<EnrollmentDTO> findByUserId(Integer userId, Pageable pageable);

    @Query(value = "SELECT e FROM Enrollment e WHERE e.user.id = ?1 AND e.course.id = ?2")
    Optional<Enrollment> getOneByUserIdAndCourseId(Integer userId, Integer courseId);

    @Transactional
    default void batchInsert(@NotEmpty List<Enrollment> enrollments, final JdbcTemplate jdbcTemplate) {
        jdbcTemplate.batchUpdate("INSERT INTO enrollments (created_at, course_id, user_id) VALUES (?, ?, ?)", enrollments,
                100, (ps, enrollment) -> {
                    int col = 1;
                    ps.setDate(col++, new Date(Instant.now().toEpochMilli()));
                    ps.setInt(col++, enrollment.getCourse().getId());
                    ps.setInt(col++, enrollment.getUser().getId());
                });
    }
}
