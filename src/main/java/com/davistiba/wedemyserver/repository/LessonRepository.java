package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    List<Lesson> getLessonsByCourse_Id(Integer courseId);

}
