package com.sachith.order_service.service.impl;

import com.sachith.order_service.client.InventoryClient;
import com.sachith.order_service.dto.CreateOrderRequest;
import com.sachith.order_service.dto.OrderItemResponse;
import com.sachith.order_service.dto.OrderResponse;
import com.sachith.order_service.model.Order;
import com.sachith.order_service.model.OrderItem;
import com.sachith.order_service.model.OrderStatus;
import com.sachith.order_service.repository.OrderRepository;
import com.sachith.order_service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final InventoryClient inventoryClient;

    @Override
    public OrderResponse create(CreateOrderRequest request) {
        Order order = new Order();
        order.setCustomerId(request.customerId());
        order.setStatus(request.status() == null ? OrderStatus.CREATED : request.status());
        order.setCurrency(request.currency());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        if (request.items() != null) {
            for (var itemRequest : request.items()) {
                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProductId(itemRequest.productId());
                item.setProductName(itemRequest.productName());
                item.setUnitPrice(itemRequest.unitPrice());
                item.setQuantity(itemRequest.quantity());

                BigDecimal unitPrice = itemRequest.unitPrice() == null ? BigDecimal.ZERO : itemRequest.unitPrice();
                int quantity = itemRequest.quantity() == null ? 0 : itemRequest.quantity();
                item.setSubtotal(unitPrice.multiply(BigDecimal.valueOf(quantity)));

                order.getItems().add(item);
            }
        }

        if (request.totalAmount() != null) {
            order.setTotalAmount(request.totalAmount());
        } else {
            BigDecimal calculatedTotal = order.getItems().stream()
                    .map(OrderItem::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            order.setTotalAmount(calculatedTotal);
        }

        return toResponse(orderRepository.save(order));
    }

    @Override
    public List<OrderResponse> getAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public Optional<OrderResponse> getById(UUID id) {
        return orderRepository.findById(id)
                .map(this::toResponse);
    }

    @Override
    public boolean deleteById(UUID id) {
        if (!orderRepository.existsById(id)) {
            return false;
        }
        orderRepository.deleteById(id);
        return true;
    }

    @CircuitBreaker(name = "inventoryService", fallbackMethod = "inventoryFallback")
    @Retry(name = "inventoryService")
    @TimeLimiter(name = "inventoryService")
    @Override
    public CompletableFuture<String> checkInventory(UUID productId) {
        return CompletableFuture.completedFuture(inventoryClient.checkInventory(productId));
    }

    public CompletableFuture<String> inventoryFallback(UUID productId, Throwable ex) {
        return CompletableFuture.completedFuture(
                "Inventory service unavailable. Please try later."
        );
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCurrency(),
                order.getItems() == null
                        ? Collections.emptyList()
                        : order.getItems().stream()
                        .map(item -> new OrderItemResponse(
                                item.getId(),
                                item.getProductId(),
                                item.getProductName(),
                                item.getUnitPrice(),
                                item.getQuantity(),
                                item.getSubtotal()
                        ))
                        .toList(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
