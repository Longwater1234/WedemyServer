package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {


}
