package com.davistiba.wedemyserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "lessons")
@Data
public class Lessons {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lessonId;

    @Column(name = "lessonName")
    @NotBlank
    private String lessonName;

    @URL
    @NotBlank
    @Column(nullable = false, unique = true)
    private String videolink;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "courseId")
    @JsonBackReference
    private Courses course;

}
