package com.sachith.inventory_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_inventory")
@Getter
@Setter
public class Inventory {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID productId;

    private Integer availableQuantity;

    private Integer reservedQuantity;

    private LocalDateTime updatedAt;
}