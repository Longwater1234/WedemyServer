package com.davistiba.wedemyserver.dto;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * REVIEW body sent from frontend
 */
@Getter
@ToString
public class ReviewRequest {

    @NotNull
    @Min(value = 1, message = "rating cannot be below 1")
    private Integer rating;

    @Size(max = 300)
    @NotEmpty
    private String content;

    @NotNull
    private Integer courseId;


}
