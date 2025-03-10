package com.davistiba.wedemyserver.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class VideoRequest {
    @NotNull
    private Integer courseId;
    @NotEmpty
    private String lessonId;

    public VideoRequest() {
    }


}
