package com.davistiba.wedemyserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "sales")
@Getter
@RequiredArgsConstructor
public class Sales {
    @Id
    @Column(name = "transaction_id", nullable = false, length = 30)
    private String transactionId;
    //^ from Braintree Payments

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private User user;

    @Column(precision = 6, scale = 2, nullable = false)
    @NotNull
    @Min(1)
    private BigDecimal totalPaid;

    @Column(nullable = false, length = 30)
    @NotBlank
    private String paymentMethod;

    @CreationTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(nullable = false)
    private Instant createdAt;

    public Sales(String transactionId, User user, BigDecimal totalPaid, String paymentMethod) {
        this.transactionId = transactionId;
        this.user = user;
        this.totalPaid = totalPaid;
        this.paymentMethod = paymentMethod.toUpperCase();
    }
}
