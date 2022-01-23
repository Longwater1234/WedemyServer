package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.dto.CategoryDTO;
import com.davistiba.wedemyserver.models.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotBlank;
import java.util.List;

public interface CourseRepository extends CrudRepository<Course, Integer> {

    List<Course> getCoursesByCategoryEquals(@NotBlank String category);

    List<Course> getTop6ByRatingGreaterThanEqual(double rating);

    List<Course> getCoursesByTitleContaining(@NotBlank String title);

    @Query(value = "SELECT DISTINCT new com.davistiba.wedemyserver.dto.CategoryDTO(c.id, c.category) FROM Course c GROUP BY c.category")
    List<CategoryDTO> getAllDistinctCategories();

    @Query(value = "SELECT c FROM Course c JOIN Wishlist w on w.course.id = c.id AND w.user.id = ?1 ORDER BY w.wishlistId DESC")
    List<Course> getCoursesWishlistByUser(Integer userId);

    @Query(value = "SELECT c FROM Course c JOIN Cart r on r.course.id = c.id AND r.user.id = ?1 ORDER BY r.id DESC")
    List<Course> getCoursesCartByUser(Integer userId);


}
