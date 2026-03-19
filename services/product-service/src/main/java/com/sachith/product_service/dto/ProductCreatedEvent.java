package com.sachith.product_service.dto;

import java.util.UUID;

public record ProductCreatedEvent(
        UUID productId,
        Integer initialAvailableQuantity) {
}