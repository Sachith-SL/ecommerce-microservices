package com.sachith.inventory_service.service;

import com.sachith.inventory_service.dto.CreateInventoryRequest;
import com.sachith.inventory_service.dto.OrderCreatedEvent;
import com.sachith.inventory_service.dto.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InventoryConsumer {

    private final InventoryService inventoryService;

    public InventoryConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(
            topics = "order-created",
            groupId = "inventory-group",
            containerFactory = "orderCreatedKafkaListenerContainerFactory"
    )
    public void consume(OrderCreatedEvent event) {
        log.info("Received order-created event: orderId={}", event.getOrderId());
        event.getItems().forEach(item ->
                log.info("Item productId={}, qty={}",
                        item.getProductId(),
                        item.getQuantity())
        );

    }

    @KafkaListener(
            topics = "product-created",
            groupId = "inventory-group",
            containerFactory = "productCreatedKafkaListenerContainerFactory"
    )
    public void consume(ProductCreatedEvent event) {
        inventoryService.createIfAbsent(new CreateInventoryRequest(
                event.productId(),
                event.initialAvailableQuantity(),
                0
        ));
        log.info("Created initial inventory for productId={} with availableQuantity={}",
                event.productId(),
                event.initialAvailableQuantity());
    }
}
