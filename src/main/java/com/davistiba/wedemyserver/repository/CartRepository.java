package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Cart;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;

public interface CartRepository extends CrudRepository<Cart, Integer> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO cart(created_at, course_id, user_id, price) VALUES (UTC_TIMESTAMP(), ?, ?, ?)", nativeQuery = true)
    Integer addToCartCustom(Integer courseId, Integer userId, BigDecimal price);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Cart c where c.user.id = ?1 and c.course.id in ?2")
    Integer deleteAllByUserIdAndCourseIdIn(Integer userId, Collection<Integer> courseId);


    @Query("SELECT (count(c) > 0) from Cart c where c.course.id = ?1 and c.user.id = ?2")
    boolean checkIfCourseInCart(Integer courseId, Integer userId);


    @Query(value = "SELECT COUNT(c) FROM Cart c WHERE c.user.id = ?1")
    Integer countCartByUserIdEquals(Integer userId);


}
