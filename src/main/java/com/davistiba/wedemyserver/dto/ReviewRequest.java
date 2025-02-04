package com.davistiba.wedemyserver.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.ToString;

/**
 * REVIEW body sent from frontend
 */
@Getter
public class ReviewRequest {

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
