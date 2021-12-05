package com.davistiba.wedemyserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table
@Data
public class CourseObjectives {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @JsonBackReference
    private Course course;

    @Size(max = 200)
    private String objective;


    public CourseObjectives(Course course, @Size(max = 200) String objective) {
        super();
        this.course = course;
        this.objective = objective;
    }

    public CourseObjectives() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseObjectives that = (CourseObjectives) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(course, that.course)) return false;
        return Objects.equals(objective, that.objective);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (course != null ? course.hashCode() : 0);
        result = 31 * result + (objective != null ? objective.hashCode() : 0);
        return result;
    }


}
