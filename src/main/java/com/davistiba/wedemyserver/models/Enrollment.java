package com.davistiba.wedemyserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "enrollments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"}))
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Course course;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private Boolean isCompleted = false;

    @ColumnDefault(value = "1")
    private Integer nextPosition = 1;

    @ColumnDefault("0")
    @Max(100)
    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal progress = BigDecimal.ZERO;

    @CreationTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant updatedAt = null;

    public Enrollment(User user, Course course) {
        this.user = user;
        this.course = course;
    }

    public BigDecimal getProgress() {
        return progress.setScale(0, RoundingMode.HALF_UP);
    }

}
