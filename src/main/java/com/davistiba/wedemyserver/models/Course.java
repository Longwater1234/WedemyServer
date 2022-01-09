package com.davistiba.wedemyserver.models;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "courses",
        indexes = {@Index(name = "IDX_CATEGORY", columnList = "category")})
@Data
public class Course implements Serializable {

    private static final long serialVersionUID = -2540907171719494221L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotBlank
    private String title;

    @Column(nullable = false, length = 100)
    @NotBlank
    @Size(max = 100)
    private String author;

    @Column(length = 50, nullable = false)
    @NotBlank
    @Size(max = 50)
    private String category;

    @Column(scale = 2, nullable = false, length = 3)
    @ColumnDefault("3.5")
    private double rating = 3.5;

    @NotBlank
    @URL
    private String thumbUrl;

    @NotBlank
    @Min(value = 10)
    @Column(nullable = false, scale = 2)
    private Double price;

    @Size(max = 250)
    private String subtitle;

}
