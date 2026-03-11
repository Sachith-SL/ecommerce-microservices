package com.sachith.order_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tbl_order_item")
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private UUID orderId;

    private UUID productId;

    private String productName;

    private BigDecimal unitPrice;

    private Integer quantity;

    private BigDecimal subtotal;
}
