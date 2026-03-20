package com.sachith.order_service.dto;

import com.sachith.order_service.model.OrderStatus;

public record UpdateOrderStatusRequest(
        OrderStatus status) {
}
