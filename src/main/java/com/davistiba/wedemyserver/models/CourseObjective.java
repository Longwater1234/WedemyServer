package com.davistiba.wedemyserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "course_objectives")
@Getter
@Setter
public class CourseObjective {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @JsonBackReference
    private Course course;

    @Size(max = 200)
    private String objective;


    public CourseObjective(Course course, @Size(max = 200) String objective) {
        super();
        this.course = course;
        this.objective = objective;
    }

    public CourseObjective() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseObjective that = (CourseObjective) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(course, that.course)) return false;
        return Objects.equals(objective, that.objective);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, objective);
    }
}
