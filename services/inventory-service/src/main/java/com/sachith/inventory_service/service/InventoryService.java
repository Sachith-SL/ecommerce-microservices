package com.sachith.inventory_service.service;

import com.sachith.inventory_service.dto.CreateInventoryRequest;
import com.sachith.inventory_service.dto.InventoryResponse;
import com.sachith.inventory_service.dto.UpdateInventoryRequest;

import java.util.Optional;
import java.util.UUID;

public interface InventoryService {

    InventoryResponse create(CreateInventoryRequest request);

    InventoryResponse createIfAbsent(CreateInventoryRequest request);

    Optional<InventoryResponse> getById(UUID id);

    Optional<InventoryResponse> updateById(UUID id, UpdateInventoryRequest request);
}
