package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Cart;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartRepository extends CrudRepository<Cart, Integer> {

    // TODO ADD PAGING LATER here
    @Query(value = "SELECT * FROM cart WHERE user_id = ?1 ORDER BY cart.id LIMIT 10", nativeQuery = true)
    List<Cart> getCartByUserId(Integer user_id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO cart(created_at, course_id, user_id, price) VALUES (UTC_TIMESTAMP(), ?, ?, ?)", nativeQuery = true)
    void addToCartCustom(Integer courseId, Integer userId, Double price);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cart WHERE course_id = ?1 AND user_id = ?2", nativeQuery = true)
    Integer deleteByCourseIdAndUserId(Integer courseId, Integer userId);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM cart WHERE course_id = ?1 AND user_id = ?2)", nativeQuery = true)
    Integer checkIfCartItemExists(Integer courseId, Integer userId);


    Integer countCartByUserId_IdEquals(Integer userId);


}
