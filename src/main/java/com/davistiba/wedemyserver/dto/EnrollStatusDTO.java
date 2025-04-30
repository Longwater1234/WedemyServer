package com.davistiba.wedemyserver.dto;

import lombok.Getter;

@Getter
public class EnrollStatusDTO {

    private final Boolean isOwned;

    public EnrollStatusDTO(Boolean isOwned) {
        this.isOwned = isOwned;
    }
}
