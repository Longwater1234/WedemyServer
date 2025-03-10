package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.dto.CategoryDTO;
import com.davistiba.wedemyserver.models.Course;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CourseRepository extends CrudRepository<Course, Integer> {

    List<Course> getCoursesByCategoryEquals(@NotBlank String category);

    List<Course> getTop6CoursesByIsFeatured(boolean isFeatured);

    Slice<Course> getCoursesByTitleContaining(@Param("title") String title, Pageable pageable);

    @Query(value = "SELECT new com.davistiba.wedemyserver.dto.CategoryDTO(MAX(c.id), c.category) FROM Course c GROUP BY c.category")
    List<CategoryDTO> getAllDistinctCategories();

    List<Course> findCoursesByIdIn(Collection<Integer> ids);

    @Query(value = "SELECT c FROM Course c JOIN Wishlist w on w.course.id = c.id AND w.user.id = ?1 ORDER BY w.id DESC")
    Page<Course> getWishlistByUser(Integer userId, Pageable pageable);

    @Query(value = "SELECT c FROM Course c JOIN Cart r on r.course.id = c.id AND r.user.id = ?1 ORDER BY r.id DESC")
    Page<Course> getCartListByUser(Integer userId, Pageable pageable);

}
