package com.sachith.inventory_service.controller;

import com.sachith.inventory_service.dto.ApiResponse;
import com.sachith.inventory_service.dto.CreateInventoryRequest;
import com.sachith.inventory_service.dto.InventoryResponse;
import com.sachith.inventory_service.dto.UpdateInventoryRequest;
import com.sachith.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/scn/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping()
    public ResponseEntity<ApiResponse<InventoryResponse>> create(@RequestBody CreateInventoryRequest request) {
        InventoryResponse inventory = inventoryService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Inventory created", inventory));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getAll() {
        List<InventoryResponse> inventoryList = inventoryService.getAll();
        return ResponseEntity.ok(new ApiResponse<>(true, "Inventory fetched", inventoryList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryResponse>> getById(@PathVariable UUID id) {
        return inventoryService.getById(id)
                .map(inventory -> ResponseEntity.ok(new ApiResponse<>(true, "Inventory fetched", inventory)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Inventory not found", null)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryResponse>> updateById(@PathVariable UUID id,
                                                                     @RequestBody UpdateInventoryRequest request) {
        return inventoryService.updateById(id, request)
                .map(inventory -> ResponseEntity.ok(new ApiResponse<>(true, "Inventory updated", inventory)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Inventory not found", null)));
    }

    @PutMapping("/product/{productId}/available/{quantity}")
    public ResponseEntity<ApiResponse<InventoryResponse>> updateAvailableQuantityByProductId(
            @PathVariable UUID productId,
            @PathVariable Integer quantity) {
        return inventoryService.updateAvailableQuantityByProductId(productId, quantity)
                .map(inventory -> ResponseEntity.ok(new ApiResponse<>(true, "Available quantity updated", inventory)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Inventory not found", null)));
    }

    @GetMapping("/check/{productId}")
    public ResponseEntity<ApiResponse<InventoryResponse>> checkInventory(@PathVariable UUID productId) {
        return inventoryService.getByProductId(productId)
                .map(inventory -> ResponseEntity.ok(new ApiResponse<>(true, "Inventory fetched", inventory)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Inventory not found", null)));
    }


}
