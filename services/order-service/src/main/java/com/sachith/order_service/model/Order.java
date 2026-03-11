package com.sachith.order_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_order")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private UUID customerId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private BigDecimal totalAmount;

    private String currency;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}