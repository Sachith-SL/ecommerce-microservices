package com.sachith.order_service.controller;

import com.sachith.order_service.client.InventoryClient;
import com.sachith.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scn/v1/test-inventory")
public class OrderInventoryTestController {

    private final OrderService orderService;

    @GetMapping("/{productId}")
    public CompletableFuture<String> testInventoryCall(@PathVariable UUID productId) {
        return orderService.checkInventory(productId);

    }
}
