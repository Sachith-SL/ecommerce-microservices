package com.sachith.order_service.service;

import com.sachith.order_service.dto.CreateOrderRequest;
import com.sachith.order_service.dto.OrderResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface OrderService {

    OrderResponse create(CreateOrderRequest request);

    List<OrderResponse> getAll();

    Optional<OrderResponse> getById(UUID id);

    boolean deleteById(UUID id);

    public CompletableFuture<String> checkInventory(UUID productId);
}
