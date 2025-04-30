package com.davistiba.wedemyserver.dto;


import lombok.Getter;

@Getter
public class CartCountResponse {

    private final Long cartCount;

    public CartCountResponse(Long cartCount) {
        this.cartCount = cartCount;
    }
}
