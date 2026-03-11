package com.sachith.order_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateOrderItemRequest(
        UUID productId,
        String productName,
        BigDecimal unitPrice,
        Integer quantity
) {
}
