package com.sachith.inventory_service.dto;

public record UpdateInventoryRequest(
        Integer availableQuantity,
        Integer reservedQuantity
) {
}
