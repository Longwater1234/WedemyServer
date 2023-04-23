package com.davistiba.wedemyserver.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.davistiba.wedemyserver.models.EnrollProgress;

@Repository
public interface EnrollProgressRepository extends CrudRepository<EnrollProgress, Long> {

	@Query("SELECT count(e) FROM EnrollProgress e WHERE e.enrollment.id = ?1")
	long countByEnrollmentId(Long id);

	@Query("SELECT e FROM EnrollProgress e WHERE e.enrollment.id = ?1 AND e.lesson.id = ?2")
	Optional<EnrollProgress> findByEnrollIdAndLessonId(Long enrollId, UUID lessonId);

}