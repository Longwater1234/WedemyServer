package com.davistiba.wedemyserver.dto;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.*;

/**
 * REVIEW body sent from frontend
 */
@Getter
@ToString
public class ReviewRequest {

    Integer id;

    @NotNull
    @Min(value = 1, message = "rating cannot be below 1")
    @Max(value = 5)
    private Integer rating;

    @Size(max = 300)
    @NotEmpty
    private String content;

    @NotNull
    private Integer courseId;


}
