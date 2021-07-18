package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Course;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

public interface CourseRepository extends CrudRepository<Course, Integer> {

    List<Course> getTopByRatingGreaterThanEqual(double rating);

    List<Course> getCoursesByCategoryEquals(@NotBlank String category);

    Optional<List<Course>> getCoursesByTitleIsLike(@NotBlank String title);
}
