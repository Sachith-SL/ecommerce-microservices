package com.sachith.order_service.dto;

import com.sachith.order_service.model.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        UUID customerId,
        OrderStatus status,
        BigDecimal totalAmount,
        String currency,
        List<CreateOrderItemRequest> items
) {
}
