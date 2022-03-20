package com.davistiba.wedemyserver.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "courses",
        indexes = {@Index(name = "IDX_CATEGORY", columnList = "category")})
@Getter
@Setter
@ToString
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

    @ColumnDefault("3.5")
    @Column(precision = 3, scale = 2, nullable = false)
    private BigDecimal rating;

    @NotBlank
    @URL
    private String thumbUrl;

    @NotNull
    @Column(nullable = false, precision = 6, scale = 2)
    @Min(1)
    private BigDecimal price;

    @Size(min = 1)
    private String subtitle;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Course course = (Course) o;
        return id != null && Objects.equals(id, course.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
