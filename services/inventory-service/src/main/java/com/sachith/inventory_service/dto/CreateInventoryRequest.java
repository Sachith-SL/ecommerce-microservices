package com.sachith.inventory_service.dto;

import java.util.UUID;

public record CreateInventoryRequest(
        UUID productId,
        Integer availableQuantity,
        Integer reservedQuantity
) {
}
