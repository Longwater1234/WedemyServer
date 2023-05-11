package com.davistiba.wedemyserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "sales")
@Getter
@RequiredArgsConstructor
public class Sales {
    @Id
    @Column(name = "transaction_id", nullable = false, length = 20)
    private String transactionId;

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
    @Column(nullable = false, columnDefinition = "DEFAULT CURRENT_TIMESTAMP")
    private Instant createdAt;


    public Sales(String transactionId, User user, BigDecimal totalPaid, String paymentMethod) {
        this.transactionId = transactionId;
        this.user = user;
        this.totalPaid = totalPaid;
        this.paymentMethod = paymentMethod.toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Sales sales = (Sales) o;
        return transactionId != null && Objects.equals(transactionId, sales.transactionId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
