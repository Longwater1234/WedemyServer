package com.davistiba.wedemyserver.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * for generating RECEIPT (on client side, ha!)
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ReceiptDTO {
    private String customerName; //transaction.getBillingAddress().getFirstName()
    private String customerEmail; //transaction.getBillingAddress().getLastName()
    private Instant paidAt; // transaction.getCreatedAt()
    private String transactionNo; // FROM URL path param
    private String merchantName;
    private BigDecimal totalPaid; // transaction.getAmount()
    private String transactionStatus; //transaction.getStatus()
    private String currency;    // transaction.getCurrencyIsoCode()
    private String paymentMethod; //transaction.getPaymentInstrumentType()
    private String recieptNo; // UUIDv4.random()
    private Instant generatedAt = Instant.now(); // current datetime in UTC

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<OrderItemDTO> orderItems; // FROM local DB. orderItemsRepository

}
