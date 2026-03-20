package com.sachith.order_service.service;

import com.sachith.order_service.dto.ApiResponse;
import com.sachith.order_service.dto.CreateOrderRequest;
import com.sachith.order_service.dto.InventoryResponse;
import com.sachith.order_service.dto.OrderResponse;
import com.sachith.order_service.model.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface OrderService {

    OrderResponse create(CreateOrderRequest request);

    List<OrderResponse> getAll();

    List<OrderResponse> getByCustomerId(UUID customerId);

    Optional<OrderResponse> getById(UUID id);

    Optional<OrderResponse> updateStatus(UUID id, OrderStatus status);

    boolean deleteById(UUID id);

    public CompletableFuture<ApiResponse<InventoryResponse>> checkInventory(UUID productId);
}
