package com.davistiba.wedemyserver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class EnrollmentDTO {
    private Long id;
    private BigDecimal progress;
    private String title;
    private String thumbUrl;
    private Integer courseId;

    public EnrollmentDTO(Long id, BigDecimal progress, String title, String thumbUrl, Integer courseId) {
        this.id = id;
        this.progress = progress;
        this.title = title;
        this.thumbUrl = thumbUrl;
        this.courseId = courseId;
    }
}
