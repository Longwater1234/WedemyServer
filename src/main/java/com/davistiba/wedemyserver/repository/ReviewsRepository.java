package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.dto.ReviewDTO;
import com.davistiba.wedemyserver.models.Reviews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewsRepository extends PagingAndSortingRepository<Reviews, Integer> {

    @Query("SELECT r FROM Reviews r WHERE r.user.id = ?1 AND r.course.id = ?2")
    Optional<Reviews> findByUserIdAndCourseId(Integer userId, Integer courseId);

    @Query("SELECT new com.davistiba.wedemyserver.dto.ReviewDTO(r.id, r.content, r.rating, r.createdAt, u.fullname) FROM Reviews r " +
            "INNER JOIN User u on r.user.id = u.id WHERE r.course.id = ?1 ORDER BY r.createdAt DESC")
    Page<ReviewDTO> findByCourseIdOrderByCreatedAtDesc(Integer courseId, Pageable pageable);


}