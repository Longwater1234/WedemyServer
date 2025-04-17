package com.davistiba.wedemyserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "lessons", uniqueConstraints = @UniqueConstraint(columnNames = {"course_id", "position"}))
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
    @JoinColumn(name = "course_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Course course;

    /**
     * convert to mm:ss
     */
    public String getLengthSeconds() {
        return String.format("%02d:%02d", Duration.ofSeconds(this.lengthSeconds).toMinutesPart(),
                Duration.ofSeconds(this.lengthSeconds).toSecondsPart());
    }

}
