package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Course;
import com.davistiba.wedemyserver.models.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    List<Lesson> getLessonsByCourse_Id(Integer courseId);

    Optional<Lesson> findByCourseEquals(Course course);


}
