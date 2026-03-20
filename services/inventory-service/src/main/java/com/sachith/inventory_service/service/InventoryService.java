package com.sachith.inventory_service.service;

import com.sachith.inventory_service.dto.CreateInventoryRequest;
import com.sachith.inventory_service.dto.InventoryResponse;
import com.sachith.inventory_service.dto.UpdateInventoryRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryService {

    InventoryResponse create(CreateInventoryRequest request);

    InventoryResponse createIfAbsent(CreateInventoryRequest request);

    List<InventoryResponse> getAll();

    Optional<InventoryResponse> getById(UUID id);

    Optional<InventoryResponse> getByProductId(UUID productId);

    Optional<InventoryResponse> updateById(UUID id, UpdateInventoryRequest request);

    Optional<InventoryResponse> updateByProductId(UUID productId, UpdateInventoryRequest request);

    Optional<InventoryResponse> updateAvailableQuantityByProductId(UUID productId, Integer quantity);

    Optional<InventoryResponse> reduceAvailableQuantityByProductId(UUID productId, Integer quantity);
}
