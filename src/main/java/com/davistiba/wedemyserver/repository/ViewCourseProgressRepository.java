package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.ViewCourseProgress;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewCourseProgressRepository extends CrudRepository<ViewCourseProgress, Integer> {
    List<ViewCourseProgress> findTop3ByUserId(Integer userId);

    List<ViewCourseProgress> findByUserId(Integer userId, Pageable pageable);
}