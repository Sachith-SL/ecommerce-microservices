package com.sachith.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderCreatedEvent {

        private String eventId;
        private String eventType;
        private Instant occurredAt;

        private UUID orderId;
        private UUID customerId;
        private String currency;
        private BigDecimal totalAmount;

        private List<OrderItemEvent> items;
}
