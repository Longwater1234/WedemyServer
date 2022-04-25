package com.davistiba.wedemyserver.models;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Immutable
@Table(name = "view_course_progress")
public class ViewCourseProgress {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "progress", precision = 5, scale = 2)
    private BigDecimal progress;

    public BigDecimal getProgress() {
        return progress.setScale(0, RoundingMode.HALF_UP);
    }

    public Integer getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public Integer getId() {
        return id;
    }
}