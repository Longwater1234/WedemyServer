package com.davistiba.wedemyserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Table(name = "enrollments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "current_lesson_id"}))
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JsonBackReference
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;


    @ManyToOne(optional = false)
    @JoinColumn(name = "current_lesson_id", referencedColumnName = "id")
    @JsonBackReference
    private Lesson currentLesson;

    @Column(nullable = false)
    private Boolean isCompleted;

    @CreationTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(nullable = false)
    private Instant updatedAt;

    public UUID getCurrentLessonId() {
        return currentLesson.getId();
    }


}
