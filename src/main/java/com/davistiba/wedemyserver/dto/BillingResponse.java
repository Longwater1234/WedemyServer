package com.davistiba.wedemyserver.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class BillingResponse {

    private final BigDecimal totalPrice;

    public BillingResponse(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
