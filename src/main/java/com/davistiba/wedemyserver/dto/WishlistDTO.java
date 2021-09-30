package com.davistiba.wedemyserver.dto;

import com.davistiba.wedemyserver.models.Course;
import lombok.Data;

import java.time.Instant;

@Data
public class WishlistDTO {
    Integer wishlistId;
    Instant createdAt;
    Course course;

    public String getCreatedAt() {
        return createdAt.toString();
    }
}
