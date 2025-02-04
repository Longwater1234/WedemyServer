package com.davistiba.wedemyserver.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;


@Getter
@EqualsAndHashCode
public class CategoryDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -248986479752746539L;
    private Integer id;
    private String category;

    public CategoryDTO(Integer id, String category) {
        this.id = id;
        this.category = category;
    }

    public CategoryDTO() {
    }


}
