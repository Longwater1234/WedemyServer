package com.davistiba.wedemyserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;

/**
 * THIS IS JOINT TABLE BETWEEN Enrollment and Lessons
 */
@Entity
@Getter
@Setter
@Table(name = "enroll_progress",
        uniqueConstraints = @UniqueConstraint(columnNames = {"enrollment_id", "lesson_id"}))
@RequiredArgsConstructor
public class EnrollProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Enrollment enrollment;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Lesson lesson;

    public EnrollProgress(Enrollment enrollment, Lesson lesson) {
        this.enrollment = enrollment;
        this.lesson = lesson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EnrollProgress that = (EnrollProgress) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
