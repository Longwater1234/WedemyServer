package com.davistiba.wedemyserver.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * NOT A TABLE.
 * This is the payload sent from Frontend at Checkout
 */
@Getter
@RequiredArgsConstructor
public class CheckoutRequest {
    @NotEmpty
    private String nonce;

    @NotNull
    @Min(1)
    private BigDecimal totalAmount;

    /**
     * "ApplePayCard" | "CreditCard" | "AndroidPayCard" | "PayPalAccount" | "VenmoAccount"
     */
    @NotEmpty
    private String paymentMethod;


}
