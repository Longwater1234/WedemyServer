package com.davistiba.wedemyserver.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class WishlistDTO {
    Integer wishlistId;
    Instant createdAt;
    String thumbUrl;
    String author;
    double price;

    public String getCreatedAt() {
        return createdAt.toString();
    }
}
