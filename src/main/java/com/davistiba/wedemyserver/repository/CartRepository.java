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
    @Query(value = "INSERT INTO cart(created_at, course_id, user_id, price) VALUES (UTC_TIMESTAMP(), ?1, ?2, ?3)", nativeQuery = true)
    int addToCartCustom(Integer courseId, Integer userId, BigDecimal price);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Cart c WHERE c.user.id = ?1 AND c.course.id IN ?2")
    int deleteByUserIdAndCoursesIn(Integer userId, Collection<Integer> courseId);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM Cart c WHERE c.user.id = ?1 AND c.course.id = ?2)")
    boolean checkIfCourseInCart(Integer userId, Integer courseId);
    //^FASTER THAN JPA EXISTS(), which USES SELECT COUNT > 0

    @Query(value = "SELECT COUNT(c) FROM Cart c WHERE c.user.id = ?1")
    long countCartByUserIdEquals(Integer userId);

    @Query(value = "SELECT SUM(c.price) FROM Cart c where c.user.id = ?1")
    BigDecimal getTotalBillForUser(Integer userId);
}
