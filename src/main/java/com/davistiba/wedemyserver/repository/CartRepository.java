package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Cart;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Repository
public interface CartRepository extends CrudRepository<Cart, Integer> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Cart c where c.user.id = ?1 and c.course.id in ?2")
    Integer deleteAllByUserIdAndCoursesIn(Integer userId, Collection<Integer> courseId);

    @Query(value = "SELECT (count(c) > 0) from Cart c where c.user.id = ?2 and c.course.id = ?1")
    boolean checkIfCourseInCart(Integer userId, Integer courseId);

    @Query(value = "SELECT COUNT(c) FROM Cart c WHERE c.user.id = ?1")
    Integer countCartByUserIdEquals(Integer userId);


}
