package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Wishlist;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends CrudRepository<Wishlist, Integer> {

    @Query(value = "SELECT * FROM wishlist w WHERE w.user_id = ?1 LIMIT 10", nativeQuery = true)
    List<Wishlist> getWishlistsByUserId(Integer user_id);


    @Query(value = "SELECT * FROM wishlist WHERE course_id = ?1 AND user_id = ?2 LIMIT 1", nativeQuery = true)
    Optional<Wishlist> checkIfWishlistExists(Integer courseId, Integer userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM wishlist WHERE course_id = ?1 AND user_id = ?2", nativeQuery = true)
    void deleteWishlistByCourseIdAndUserId(Integer courseId, Integer userId);


}
