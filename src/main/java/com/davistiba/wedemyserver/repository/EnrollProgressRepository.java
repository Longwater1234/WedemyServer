package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.EnrollProgress;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnrollProgressRepository extends CrudRepository<EnrollProgress, Long> {

    @Query("select count(e) from EnrollProgress e where e.enrollment.id = ?1")
    long countByEnrollmentId(Integer id);

    @Query("select e from EnrollProgress e where e.enrollment.id = ?1 and e.lesson.id = ?2")
    Optional<EnrollProgress> findByEnrollIdAndLessonId(Long enrollId, UUID lessonId);

    @Query("select e from EnrollProgress e where e.enrollment.id = ?1 ORDER BY e.id DESC")
    List<EnrollProgress> findFirstByEnrollmentId(Integer enrollId, Pageable pageable);
}