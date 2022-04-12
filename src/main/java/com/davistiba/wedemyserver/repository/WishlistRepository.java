package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Wishlist;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface WishlistRepository extends CrudRepository<Wishlist, Integer> {

    @Query(value = "SELECT (COUNT(w) > 0) FROM Wishlist w WHERE w.user.id = ?1 AND w.course.id = ?2")
    boolean checkIfCourseInWishlist(Integer userId, Integer courseId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Wishlist w WHERE w.course.id = ?1 AND w.user.id = ?2")
    Integer deleteByCourseIdAndUserId(Integer courseId, Integer userId);


}
