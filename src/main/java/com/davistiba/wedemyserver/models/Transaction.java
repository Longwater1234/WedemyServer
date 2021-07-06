package com.davistiba.wedemyserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Entity
@Table(name = "transactions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"}))
@Data
public class Transaction {
    @Id
    @Column(length = 30, updatable = false)
    private String transactionId;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    @JsonBackReference
    private User userId;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "course_id", referencedColumnName = "courseId")
    private Course course;

    @Column(name = "totalprice", scale = 2, nullable = false)
    @NotBlank
    private float totalPrice;

    @CreationTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "createdAt")
    private Instant createdAt;


}
