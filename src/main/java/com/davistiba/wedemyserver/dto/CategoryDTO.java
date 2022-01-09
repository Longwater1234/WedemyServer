package com.davistiba.wedemyserver.dto;

import lombok.Data;

@Data
public class CategoryDTO {
    private Integer id;
    private String category;

    public CategoryDTO(Integer id, String category) {
        this.id = id;
        this.category = category;
    }

    public CategoryDTO() {
    }
}
