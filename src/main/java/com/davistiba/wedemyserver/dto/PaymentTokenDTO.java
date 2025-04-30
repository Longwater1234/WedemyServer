package com.davistiba.wedemyserver.dto;

import lombok.Getter;

@Getter
public class PaymentTokenDTO {

    private final String clientToken;

    public PaymentTokenDTO(String clientToken) {
        this.clientToken = clientToken;
    }
}
