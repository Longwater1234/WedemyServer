package com.davistiba.wedemyserver.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * This is for updating watchStatus on currentLessonId, and requesting next lesson
 */
@Getter
public class WatchStatusReq {
    @NotNull
    private Long enrollId;
    @NotNull
    private String currentLessonId;
    @NotNull
    private Integer courseId;

    public WatchStatusReq() {
    }
}
