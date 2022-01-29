package com.davistiba.wedemyserver.dto;

import com.davistiba.wedemyserver.models.Course;
import lombok.Data;

import java.time.Instant;

@Data
public class OrdersDTO {
    private String transactionId;
    private Course course;
    private Instant createdAt;
    private String paymentMethod;
}
