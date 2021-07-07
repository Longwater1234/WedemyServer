package com.davistiba.wedemyserver.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "courseID")
    private Integer courseId;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String title;

    @Column(length = 50, nullable = false)
    @NotBlank
    private String category;

    @Column(scale = 2)
    private double rating = 0.0;

    @NotBlank
    @Column(nullable = false)
    private String thumbUrl;

    @NotBlank
    @Column(length = 500, nullable = false)
    private String description;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Lesson> lessonList;


}
