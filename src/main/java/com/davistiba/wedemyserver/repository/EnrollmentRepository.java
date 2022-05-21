package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Enrollment;
import com.davistiba.wedemyserver.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    @Query("SELECT (COUNT(e) > 0) FROM Enrollment e WHERE e.user.id = ?1 AND e.course.id = ?2")
    boolean existsByCourseIdAndUserId(Integer userId, Integer courseId);

    long countEnrollmentByUserAndIsCompleted(User user, Boolean isCompleted);

    long countEnrollmentByUser(User user);

    @Query(value = "SELECT e FROM Enrollment e WHERE e.user.id = ?1 AND e.course.id = ?2")
    Optional<Enrollment> getByUserIdAndCourseId(Integer userId, Integer courseId);


}
