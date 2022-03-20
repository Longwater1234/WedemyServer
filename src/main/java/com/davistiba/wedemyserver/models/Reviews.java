package com.davistiba.wedemyserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "reviews",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"}))
public class Reviews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(precision = 3, scale = 2, nullable = false)
    @Max(5)
    private BigDecimal rating;

    @Column(nullable = false)
    @Size(max = 250)
    private String content;

    @ManyToOne(optional = false)
    @JsonBackReference
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @ToString.Exclude
    private Course course;

    @CreationTimestamp
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdAt;

    @UpdateTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(nullable = false)
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Reviews reviews = (Reviews) o;
        return id != null && Objects.equals(id, reviews.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
