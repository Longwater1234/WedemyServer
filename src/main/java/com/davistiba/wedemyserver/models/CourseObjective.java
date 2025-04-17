package com.davistiba.wedemyserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "course_objectives")
@Getter
@Setter
@RequiredArgsConstructor
public class CourseObjective {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id")
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Course course;

    @Size(max = 200)
    private String objective;

    public CourseObjective(Course course, String objective) {
        super();
        this.course = course;
        this.objective = objective;
    }
}
