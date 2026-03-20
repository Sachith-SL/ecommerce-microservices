package com.sachith.order_service.service.impl;

import com.sachith.order_service.client.ProductClient;
import com.sachith.order_service.client.InventoryClient;
import com.sachith.order_service.dto.ApiResponse;
import com.sachith.order_service.dto.CreateOrderRequest;
import com.sachith.order_service.dto.InventoryResponse;
import com.sachith.order_service.dto.OrderCreatedEvent;
import com.sachith.order_service.dto.OrderItemEvent;
import com.sachith.order_service.dto.OrderItemResponse;
import com.sachith.order_service.dto.OrderResponse;
import com.sachith.order_service.dto.ProductResponse;
import com.sachith.order_service.model.Order;
import com.sachith.order_service.model.OrderItem;
import com.sachith.order_service.model.OrderStatus;
import com.sachith.order_service.repository.OrderRepository;
import com.sachith.order_service.service.OrderProducer;
import com.sachith.order_service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
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

    private final ProductClient productClient;

    private final InventoryClient inventoryClient;

    private final OrderProducer orderProducer;

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
                validateProductIsActive(itemRequest.productId());

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

        Order savedOrder = orderRepository.save(order);
        List<OrderItem> items = savedOrder
                .getItems() == null ? Collections.emptyList() : savedOrder.getItems();

        OrderCreatedEvent event = mapToEvent(savedOrder, items);
        orderProducer.sendOrderCreatedEvent(event);

        return toResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<OrderResponse> getByCustomerId(UUID customerId) {
        return orderRepository.findByCustomerId(customerId)
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
    public Optional<OrderResponse> updateStatus(UUID id, OrderStatus status) {
        if (status == null) {
            throw new RuntimeException("Order status is required");
        }

        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(status);
                    order.setUpdatedAt(LocalDateTime.now());
                    return orderRepository.save(order);
                })
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
    public CompletableFuture<ApiResponse<InventoryResponse>> checkInventory(UUID productId) {
        return CompletableFuture.completedFuture(inventoryClient.checkInventory(productId));
    }

    public CompletableFuture<ApiResponse<InventoryResponse>> inventoryFallback(UUID productId, Throwable ex) {
        return CompletableFuture.completedFuture(
                new ApiResponse<>(false, "Inventory service unavailable. Please try later.", null));
    }

    private void validateProductIsActive(UUID productId) {
        if (productId == null) {
            throw new RuntimeException("Product id is required");
        }

        ApiResponse<ProductResponse> productResponse = productClient.checkProduct(productId);
        if (productResponse == null || !productResponse.isSuccess() || productResponse.getData() == null) {
            throw new RuntimeException("Product not found");
        }

        if (!Boolean.TRUE.equals(productResponse.getData().active())) {
            throw new RuntimeException("Product is inactive");
        }
    }

    private OrderCreatedEvent mapToEvent(Order savedOrder, List<OrderItem> items) {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setEventType("ORDER_CREATED");
        event.setOccurredAt(Instant.now());
        event.setOrderId(savedOrder.getId());
        event.setCustomerId(savedOrder.getCustomerId());
        event.setCurrency(savedOrder.getCurrency());
        event.setTotalAmount(savedOrder.getTotalAmount());
        event.setItems(items.stream().map(this::mapToItemEvent).toList());
        return event;
    }

    private OrderItemEvent mapToItemEvent(OrderItem item) {
        OrderItemEvent eventItem = new OrderItemEvent();
        eventItem.setProductId(item.getProductId());
        eventItem.setProductName(item.getProductName());
        eventItem.setUnitPrice(item.getUnitPrice());
        eventItem.setQuantity(item.getQuantity());
        eventItem.setSubtotal(item.getSubtotal());
        return eventItem;
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
                                        item.getSubtotal()))
                                .toList(),
                order.getCreatedAt(),
                order.getUpdatedAt());
    }
}
