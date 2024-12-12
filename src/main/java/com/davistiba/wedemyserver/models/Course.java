package com.davistiba.wedemyserver.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.URL;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "courses",
        indexes = {@Index(name = "IDX_CATEGORY", columnList = "category")})
@Getter
@Setter
@RequiredArgsConstructor
public class Course implements Serializable {

    @Serial
    private static final long serialVersionUID = -2540907171719494221L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotBlank
    private String title;

    private String subtitle;

    @Column(nullable = false, length = 100)
    @NotBlank
    @Size(max = 100)
    private String author;

    @Column(length = 50, nullable = false)
    @NotBlank
    @Size(max = 50)
    private String category;

    @ColumnDefault("0.0")
    @Column(precision = 6, scale = 2, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal rating;

    @NotBlank
    @URL
    private String thumbUrl;

    @NotNull
    @Column(nullable = false, precision = 6, scale = 2)
    @Min(1)
    private BigDecimal price;

    @ColumnDefault(value = "FALSE")
    @Column(nullable = false)
    private Boolean isFeatured = false;

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
