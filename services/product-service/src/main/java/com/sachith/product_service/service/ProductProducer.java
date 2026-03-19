package com.sachith.product_service.service;

import com.sachith.product_service.dto.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductProducer {

    private static final String TOPIC_NAME = "product-created";

    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public void sendProductCreatedEvent(ProductCreatedEvent event) {
        kafkaTemplate.send(TOPIC_NAME, event.productId().toString(), event);
    }
}