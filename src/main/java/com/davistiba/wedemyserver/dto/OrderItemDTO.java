package com.davistiba.wedemyserver.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class OrderItemDTO {
    private Long id;
    private String title;
    private BigDecimal price;

    public OrderItemDTO(Long id, String title, BigDecimal price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }
}
