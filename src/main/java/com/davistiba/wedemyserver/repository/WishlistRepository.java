package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Wishlist;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Repository
public interface WishlistRepository extends CrudRepository<Wishlist, Integer> {

    @Query(value = "SELECT EXISTS(SELECT 1 FROM Wishlist w WHERE w.user.id = ?1 AND w.course.id = ?2)")
    boolean checkIfExistWishlistNative(Integer userId, Integer courseId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Wishlist w where w.user.id = ?1 and w.course.id in ?2")
    int deleteByUserIdAndCoursesIn(Integer userId, Collection<Integer> courseId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO wishlist(created_at, course_id, user_id) VALUES (UTC_TIMESTAMP(), ?, ?)", nativeQuery = true)
    int saveByCourseIdAndUserId(Integer courseId, Integer userId);


}
