package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Wishlist;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface WishlistRepository extends CrudRepository<Wishlist, Integer> {

    @Query("SELECT (COUNT(w) > 0) FROM Wishlist w WHERE w.user.id = ?1 AND w.course.id = ?2")
    boolean checkIfCourseInWishlist(Integer userId, Integer courseId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO wishlist(created_at, course_id, user_id) VALUES (UTC_TIMESTAMP(), ?, ?)", nativeQuery = true)
    Integer saveByCourseIdAndUserId(Integer courseId, Integer userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM wishlist WHERE course_id = ?1 AND user_id = ?2", nativeQuery = true)
    Integer deleteByCourseIdAndUserId(Integer courseId, Integer userId);


}
