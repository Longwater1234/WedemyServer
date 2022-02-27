package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    @Query("SELECT (COUNT(e) > 0) FROM Enrollment e WHERE e.userId.id = ?1 AND e.course.id = ?2")
    boolean existsByCourseIdAndUserId(Integer userId, Integer courseId);


}
