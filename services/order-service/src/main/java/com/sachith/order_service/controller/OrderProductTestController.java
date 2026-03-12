package com.sachith.order_service.controller;

import com.sachith.order_service.client.InventoryClient;
import com.sachith.order_service.client.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scn/v1/test-product")
public class OrderProductTestController {
    private final ProductClient productClient;

    @GetMapping("/{productId}")
    public String testInventoryCall(@PathVariable UUID productId) {
        return productClient.checkProduct(productId);
    }
}
