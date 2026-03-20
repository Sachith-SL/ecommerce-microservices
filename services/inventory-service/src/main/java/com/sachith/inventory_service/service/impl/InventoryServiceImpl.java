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
import java.util.List;
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
    public List<InventoryResponse> getAll() {
        return inventoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public Optional<InventoryResponse> getById(UUID id) {
        return inventoryRepository.findById(id)
                .map(this::toResponse);
    }

    @Override
    public Optional<InventoryResponse> getByProductId(UUID productId) {
        return inventoryRepository.findByProductId(productId)
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

    @Override
    public Optional<InventoryResponse> updateByProductId(UUID productId, UpdateInventoryRequest request) {
        return inventoryRepository.findByProductId(productId)
                .map(inventory -> {
                    inventory.setAvailableQuantity(request.availableQuantity());
                    inventory.setReservedQuantity(request.reservedQuantity());
                    inventory.setUpdatedAt(LocalDateTime.now());
                    return toResponse(inventoryRepository.save(inventory));
                });
    }

    @Override
    public Optional<InventoryResponse> updateAvailableQuantityByProductId(UUID productId, Integer quantity) {
        if (quantity == null || quantity < 0) {
            throw new RuntimeException("Quantity must be zero or greater");
        }

        return inventoryRepository.findByProductId(productId)
                .map(inventory -> {
                    inventory.setAvailableQuantity(quantity);
                    inventory.setUpdatedAt(LocalDateTime.now());
                    return toResponse(inventoryRepository.save(inventory));
                });
    }

    @Override
    public Optional<InventoryResponse> reduceAvailableQuantityByProductId(UUID productId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }

        return inventoryRepository.findByProductId(productId)
                .map(inventory -> {
                    int currentAvailable = inventory.getAvailableQuantity() == null
                            ? 0
                            : inventory.getAvailableQuantity();

                    if (currentAvailable < quantity) {
                        throw new RuntimeException("Insufficient inventory");
                    }

                    inventory.setAvailableQuantity(currentAvailable - quantity);
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
