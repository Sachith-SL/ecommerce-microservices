package com.sachith.inventory_service.service;

import com.sachith.inventory_service.dto.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InventoryConsumer {

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
}
