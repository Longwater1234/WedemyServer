package com.davistiba.wedemyserver.dto;

import lombok.Getter;

@Getter
public class CartCheckResponse {

    private final Boolean inCart;

    public CartCheckResponse(Boolean inCart) {
        this.inCart = inCart;
    }
}
