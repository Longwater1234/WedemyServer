package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Wishlist;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WishlistRepository extends CrudRepository<Wishlist, Integer> {

    // TODO ADD PAGING LATER here
    @Query(value = "SELECT * FROM wishlist WHERE user_id = ?1 ORDER BY wishlist_id LIMIT 10", nativeQuery = true)
    List<Wishlist> getWishlistsByUserId(Integer user_id);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM wishlist WHERE course_id = ?1 AND user_id = ?2)", nativeQuery = true)
    Integer checkIfWishlistExists(Integer courseId, Integer userId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO wishlist(created_at, course_id, user_id) VALUES (UTC_TIMESTAMP(), ?, ?)", nativeQuery = true)
    Integer saveByCourseIdAndUserId(Integer courseId, Integer userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM wishlist WHERE course_id = ?1 AND user_id = ?2", nativeQuery = true)
    Integer deleteByCourseIdAndUserId(Integer courseId, Integer userId);


}
