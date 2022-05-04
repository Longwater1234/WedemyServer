package com.davistiba.wedemyserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    @NotBlank
    private String lessonName;

    @NotBlank
    @Column(nullable = false, unique = true, length = 20)
    @Size(max = 20)
    private String videokey;

    @NotNull
    @ColumnDefault("0")
    private Integer lengthSeconds;

    @NotNull
    @Column(nullable = false)
    private Integer position;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @JsonBackReference
    private Course course;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Lesson lesson = (Lesson) o;
        return id != null && Objects.equals(id, lesson.id);
    }

    public String getLengthSeconds() {
        return String.format("%02d:%02d", Duration.ofSeconds(this.lengthSeconds).toMinutesPart(),
                Duration.ofSeconds(this.lengthSeconds).toSecondsPart());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
