package com.davistiba.wedemyserver.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@EqualsAndHashCode
@ToString
public class EnrollmentDTO {
    private Integer id;
    private BigDecimal progress;
    private String title;
    private String thumbUrl;
    private Integer courseId;

    public EnrollmentDTO() {
    }

    public EnrollmentDTO(Integer id, BigDecimal progress, String title, String thumbUrl, Integer courseId) {
        this.id = id;
        this.progress = progress;
        this.title = title;
        this.thumbUrl = thumbUrl;
        this.courseId = courseId;
    }
}
