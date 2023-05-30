package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Cart;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;

@Repository
public interface CartRepository extends CrudRepository<Cart, Integer> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO cart(created_at, course_id, user_id, price) VALUES (UTC_TIMESTAMP(), ?, ?, ?)", nativeQuery = true)
    Integer addToCartCustom(Integer courseId, Integer userId, BigDecimal price);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Cart c where c.user.id = ?1 and c.course.id in ?2")
    Integer deleteByUserIdAndCoursesIn(Integer userId, Collection<Integer> courseId);

    @Query(value = "SELECT EXISTS(SELECT 1 from cart c where c.user_id = ?1 and c.course_id = ?2)", nativeQuery = true)
    long checkIfCourseInCart(Integer userId, Integer courseId);

    @Query(value = "SELECT COUNT(c) FROM Cart c WHERE c.user.id = ?1")
    long countCartByUserIdEquals(Integer userId);

    @Query(value = "SELECT SUM(c.price) FROM Cart c where c.user.id = ?1")
    BigDecimal getTotalBillForUser(Integer userId);
}
