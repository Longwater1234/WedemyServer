package com.davistiba.wedemyserver.repository;

import com.davistiba.wedemyserver.models.CourseObjective;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ObjectiveRepository extends CrudRepository<CourseObjective, Integer> {

    List<CourseObjective> getCourseObjectivesByCourseId(Integer courseId);

    @Transactional
    default void batchInsert(List<CourseObjective> courseObjectives, JdbcTemplate jdbcTemplate) {
        jdbcTemplate.batchUpdate("INSERT INTO course_objectives (objective, course_id) VALUES (?, ?)",
                courseObjectives, courseObjectives.size(), (ps, arg) -> {
                    int col = 1;
                    ps.setString(col++, arg.getObjective());
                    ps.setInt(col++, arg.getCourse().getId());
                });
    }
}
