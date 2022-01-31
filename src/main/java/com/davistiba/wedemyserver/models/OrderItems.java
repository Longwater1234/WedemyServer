package com.davistiba.wedemyserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "order_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"}))
@Getter
@Setter
@RequiredArgsConstructor
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private User userId;

    @Column(precision = 6, scale = 2, nullable = false)
    @NotBlank
    private BigDecimal totalPaid;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enum('PAYPAL', 'CREDIT_CARD')")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private PaymentMethod paymentMethod;

    @Column(length = 20, updatable = false, nullable = false)
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String transactionId; //<-- from BRAINTREE


    @CreationTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(nullable = false)
    private Instant createdAt;

    public String getPaymentMethod() {
        return paymentMethod.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OrderItems orderItems = (OrderItems) o;
        return id != null && Objects.equals(id, orderItems.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
