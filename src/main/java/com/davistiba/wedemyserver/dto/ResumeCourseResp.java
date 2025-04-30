package com.davistiba.wedemyserver.dto;

import lombok.Getter;

@Getter
public class ResumeCourseResp {

    private final String lessonId;

    public ResumeCourseResp(String lessonId) {
        this.lessonId = lessonId;
    }
}
