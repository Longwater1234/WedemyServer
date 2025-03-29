package com.davistiba.wedemyserver.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * NOT A TABLE
 * this is for customer's Purchase History
 */
@Getter
@RequiredArgsConstructor
public class SalesDTO {
    private String transactionId;
    private Instant createdAt;
    private String paymentMethod;
    private BigDecimal totalPaid;
    private Long numOfItems;


    public SalesDTO(String transactionId, Instant createdAt, String paymentMethod, BigDecimal totalPaid, Long numOfItems) {
        this.transactionId = transactionId;
        this.createdAt = createdAt;
        this.paymentMethod = paymentMethod;
        this.totalPaid = totalPaid;
        this.numOfItems = numOfItems;
    }
}
