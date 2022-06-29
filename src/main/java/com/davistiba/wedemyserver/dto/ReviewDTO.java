package com.davistiba.wedemyserver.dto;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@ToString
public class ReviewDTO {
    private Integer id;
    private String content;
    private Integer rating;
    private Instant createdAt;
    private String fullname;

    public ReviewDTO(Integer id, String content, Integer rating, Instant createdAt, String fullname) {
        this.id = id;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
        this.fullname = fullname;
    }

    public ReviewDTO() {
    }
}
