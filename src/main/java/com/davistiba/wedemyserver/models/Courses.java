package com.davistiba.wedemyserver.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
public class Courses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "courseID")
    private Integer courseId;

    @Column(length = 50, unique = true)
    @NotBlank
    private String title;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Lessons> lessonList;
}
