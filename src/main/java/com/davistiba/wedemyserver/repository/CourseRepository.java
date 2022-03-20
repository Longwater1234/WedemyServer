package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.dto.CategoryDTO;
import com.davistiba.wedemyserver.models.Course;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Repository
public interface CourseRepository extends CrudRepository<Course, Integer> {

    List<Course> getCoursesByCategoryEquals(@NotBlank String category);

    List<Course> getTop6ByRatingGreaterThanEqual(BigDecimal rating);

    List<Course> getCoursesByTitleContaining(@NotBlank String title);

    @Query(value = "SELECT DISTINCT new com.davistiba.wedemyserver.dto.CategoryDTO(c.id, c.category) FROM Course c GROUP BY c.category")
    List<CategoryDTO> getAllDistinctCategories();

    List<Course> findCoursesByIdIn(Collection<Integer> ids);

    @Query(value = "SELECT c FROM Course c JOIN Wishlist w on w.course.id = c.id AND w.user.id = ?1 ORDER BY w.wishlistId DESC")
    List<Course> getCoursesWishlistByUser(Integer userId, Pageable pageable);

    @Query(value = "SELECT c FROM Course c JOIN Cart r on r.course.id = c.id AND r.user.id = ?1 ORDER BY r.id DESC")
    List<Course> getCoursesCartByUser(Integer userId, Pageable pageable);


}
