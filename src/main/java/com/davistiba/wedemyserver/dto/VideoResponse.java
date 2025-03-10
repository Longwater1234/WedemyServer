package com.davistiba.wedemyserver.dto;

import com.davistiba.wedemyserver.models.Lesson;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class VideoResponse {
    @NotNull
    private final Long enrollId;
    @NotNull
    private final Lesson lesson;

    public VideoResponse(Long enrollId, Lesson lesson) {
        this.enrollId = enrollId;
        this.lesson = lesson;
    }
}
