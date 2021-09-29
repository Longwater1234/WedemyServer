package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Course;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotBlank;
import java.util.List;

public interface CourseRepository extends CrudRepository<Course, Integer> {

    List<Course> getCoursesByCategoryEquals(@NotBlank String category);

    List<Course> getTop6ByRatingGreaterThanEqual(double rating);

    List<Course> getCoursesByTitleContaining(@NotBlank String title);
}
