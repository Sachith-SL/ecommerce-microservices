package com.sachith.inventory_service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record InventoryResponse(
        UUID id,
        UUID productId,
        Integer availableQuantity,
        Integer reservedQuantity,
        LocalDateTime updatedAt
) {
}
