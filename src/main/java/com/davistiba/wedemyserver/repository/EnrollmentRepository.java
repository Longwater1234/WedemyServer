package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.dto.EnrollmentDTO;
import com.davistiba.wedemyserver.models.Enrollment;
import com.davistiba.wedemyserver.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends CrudRepository<Enrollment, Long> {
    @Query("SELECT (COUNT(e) > 0) FROM Enrollment e WHERE e.user.id = ?1 AND e.course.id = ?2")
    boolean existsByUserIdAndCourseId(Integer userId, Integer courseId);

    long countEnrollmentByUserAndIsCompleted(User user, Boolean isCompleted);

    long countEnrollmentByUser(User user);

    @Query(value = "SELECT e FROM Enrollment e WHERE e.user.id = ?1 AND e.id = ?2")
    Optional<Enrollment> getByUserIdAndEnrollId(Integer userId, Integer enrollId);

    @Query("SELECT new com.davistiba.wedemyserver.dto.EnrollmentDTO(e.id, e.progress, c.title, c.thumbUrl, c.id) FROM Enrollment e " +
            "INNER JOIN Course c ON e.course.id = c.id WHERE e.user.id = ?1 ORDER BY e.id DESC")
    List<EnrollmentDTO> findByUserId(Integer userId, Pageable pageable);
    
    @Query(value = "SELECT e FROM Enrollment e WHERE e.user.id = ?1 AND e.course.id = ?2")
    Optional<Enrollment> getByUserIdAndCourseId(Integer userId, Integer courseId);

}
