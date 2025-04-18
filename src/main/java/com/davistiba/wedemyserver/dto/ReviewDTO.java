package com.davistiba.wedemyserver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
public class ReviewDTO {
    private Integer id;
    private String content;
    private Integer rating;
    private Instant updatedAt;
    private String fullname;

    public ReviewDTO(Integer id, String content, Integer rating, Instant updatedAt, String fullname) {
        this.id = id;
        this.content = content;
        this.rating = rating;
        this.updatedAt = updatedAt;
        this.fullname = fullname;
    }

}
