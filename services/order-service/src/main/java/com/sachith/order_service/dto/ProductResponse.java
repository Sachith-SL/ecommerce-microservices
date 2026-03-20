package com.sachith.order_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String sku,
        String name,
        String description,
        String category,
        String productType,
        String unitOfMeasure,
        BigDecimal unitPrice,
        String currency,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}