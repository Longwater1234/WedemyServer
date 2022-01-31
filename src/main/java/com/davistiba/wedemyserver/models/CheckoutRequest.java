package com.davistiba.wedemyserver.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * NOT A TABLE.
 * This is the payload sent by client on Checkout
 */
@Getter
@ToString
@EqualsAndHashCode
public class CheckoutRequest {
    @NotBlank
    private String nonce;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private Integer courseId;
}
