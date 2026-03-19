package com.sachith.inventory_service.service.impl;

import com.sachith.inventory_service.dto.CreateInventoryRequest;
import com.sachith.inventory_service.dto.InventoryResponse;
import com.sachith.inventory_service.dto.UpdateInventoryRequest;
import com.sachith.inventory_service.model.Inventory;
import com.sachith.inventory_service.repository.InventoryRepository;
import com.sachith.inventory_service.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public InventoryResponse create(CreateInventoryRequest request) {
        Inventory inventory = new Inventory();
        inventory.setProductId(request.productId());
        inventory.setAvailableQuantity(request.availableQuantity());
        inventory.setReservedQuantity(request.reservedQuantity());
        inventory.setUpdatedAt(LocalDateTime.now());

        return toResponse(inventoryRepository.save(inventory));
    }

    @Override
    @Transactional
    public InventoryResponse createIfAbsent(CreateInventoryRequest request) {
        return inventoryRepository.findByProductId(request.productId())
                .map(this::toResponse)
                .orElseGet(() -> create(request));
    }

    @Override
    public Optional<InventoryResponse> getById(UUID id) {
        return inventoryRepository.findById(id)
                .map(this::toResponse);
    }

    @Override
    public Optional<InventoryResponse> updateById(UUID id, UpdateInventoryRequest request) {
        return inventoryRepository.findById(id)
                .map(inventory -> {
                    inventory.setAvailableQuantity(request.availableQuantity());
                    inventory.setReservedQuantity(request.reservedQuantity());
                    inventory.setUpdatedAt(LocalDateTime.now());
                    return toResponse(inventoryRepository.save(inventory));
                });
    }

    private InventoryResponse toResponse(Inventory inventory) {
        return new InventoryResponse(
                inventory.getId(),
                inventory.getProductId(),
                inventory.getAvailableQuantity(),
                inventory.getReservedQuantity(),
                inventory.getUpdatedAt()
        );
    }
}
