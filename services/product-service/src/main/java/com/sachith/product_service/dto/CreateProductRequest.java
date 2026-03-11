package com.sachith.product_service.dto;

import java.math.BigDecimal;

public record CreateProductRequest(
        String sku,
        String name,
        String description,
        String category,
        String productType,
        String unitOfMeasure,
        BigDecimal unitPrice,
        String currency,
        Boolean active
) {
}
