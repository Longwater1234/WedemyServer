package com.davistiba.wedemyserver.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "courses",
        indexes = {@Index(name = "IDX_CATEGORY", columnList = "category")})
@Data

public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;

    @Column(nullable = false)
    @NotBlank
    private String title;

    @Column(nullable = false, length = 50)
    @NotBlank
    private String author;

    @Column(length = 50, nullable = false)
    @NotBlank
    private String category;

    @Column(scale = 2, nullable = false)
    @ColumnDefault("3.5")
    private double rating = 3.5;

    @NotBlank
    @URL
    private String thumbUrl;

    @NotBlank
    @Min(value = 10)
    @Column(nullable = false, scale = 2)
    private double price;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Lesson> lessonList;


}
