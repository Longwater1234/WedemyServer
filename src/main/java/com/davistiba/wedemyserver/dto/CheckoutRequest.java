package com.davistiba.wedemyserver.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * NOT A TABLE.
 * This is the payload sent from Frontend at Checkout
 */
@Getter
@ToString
@EqualsAndHashCode
public class CheckoutRequest {
    @NotEmpty
    private String nonce;

    @NotNull
    private BigDecimal totalAmount;

    @NotEmpty
    private ArrayList<Integer> courses;

    @NotEmpty
    private String paymentMethod;
    // "ApplePayCard" | "CREDITCARD" | "AndroidPayCard" | "PayPalAccount" | "VenmoAccount"

    public CheckoutRequest() {
    }

}
