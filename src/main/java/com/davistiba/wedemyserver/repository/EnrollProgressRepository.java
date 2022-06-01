package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.EnrollProgress;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EnrollProgressRepository extends CrudRepository<EnrollProgress, Long> {

    @Modifying
    @Query(value = "INSERT INTO enroll_progress(enrollment_id, lesson_id) VALUES (?, ?)", nativeQuery = true)
    EnrollProgress saveCustom(Integer enrollId, UUID lessonId);

    @Query("select count(e) from EnrollProgress e where e.enrollment.id = ?1")
    long countByEnrollmentId(Integer id);


}