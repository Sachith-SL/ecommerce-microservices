package com.sachith.product_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_product")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(unique = true, nullable = false)
    private String sku;

    private String name;

    private String description;

    private String category;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type")
    private ProductType productType;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_of_measure")
    private UnitOfMeasure unitOfMeasure;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    private String currency;

    private Boolean active;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

/*

Sample product Record

id: 7e7f23e2-92c0
sku: MILK-ANCHOR-1L
name: Anchor Fresh Milk
description: Fresh dairy milk
category: DAIRY
product_type: REFRIGERATED
unit_of_measure: LITER
unit_price: 3.50
currency: USD
active: true
created_at: 2026-03-11
updated_at: 2026-03-11

*/
