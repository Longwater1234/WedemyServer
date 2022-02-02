package com.davistiba.wedemyserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "sales",
        indexes = {@Index(name = "IDX_USER_ID", columnList = "user_id")})
@Getter
@Setter
@RequiredArgsConstructor
public class Sales {
    @Id
    @Column(name = "transaction_id", nullable = false, length = 20)
    private String transactionId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private User userId;

    @Column(precision = 6, scale = 2, nullable = false)
    @NotNull
    private BigDecimal totalPaid;

    @Column(nullable = false, length = 30)
    @NotBlank
    private String paymentMethod;

    @CreationTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    @JsonManagedReference
    List<OrderItem> orderItemList;


    public Sales(String transactionId, User userId, BigDecimal totalPaid, String paymentMethod) {
        this.transactionId = transactionId;
        this.userId = userId;
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
