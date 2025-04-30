package com.davistiba.wedemyserver.dto;

import lombok.Getter;

@Getter
public class WishlistCheckResp {

    private final Boolean inWishlist;

    public WishlistCheckResp(Boolean inWishlist) {
        this.inWishlist = inWishlist;
    }
}
