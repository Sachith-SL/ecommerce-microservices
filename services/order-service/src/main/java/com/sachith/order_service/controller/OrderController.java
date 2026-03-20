package com.sachith.order_service.controller;

import com.sachith.order_service.dto.ApiResponse;
import com.sachith.order_service.dto.CreateOrderRequest;
import com.sachith.order_service.dto.OrderResponse;
import com.sachith.order_service.dto.UpdateOrderStatusRequest;
import com.sachith.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/scn/v1/order")
@RequiredArgsConstructor
public class OrderController {

        private final OrderService orderService;

        @PostMapping
        public ResponseEntity<ApiResponse<OrderResponse>> create(@RequestBody CreateOrderRequest request) {
                OrderResponse order = orderService.create(request);
                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(new ApiResponse<>(
                                                true,
                                                "Order created",
                                                order));
        }

        @GetMapping
        public ResponseEntity<ApiResponse<List<OrderResponse>>> getAll(
                        @RequestParam(required = false) UUID customerId) {
                List<OrderResponse> orders = customerId == null
                                ? orderService.getAll()
                                : orderService.getByCustomerId(customerId);
                return ResponseEntity.ok(new ApiResponse<>(
                                true,
                                customerId == null ? "Orders fetched" : "Customer orders fetched",
                                orders));
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<OrderResponse>> getById(@PathVariable UUID id) {
                return orderService.getById(id)
                                .map(order -> ResponseEntity.ok(new ApiResponse<>(
                                                true,
                                                "Order fetched",
                                                order)))
                                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                                                .body(new ApiResponse<>(false, "Order not found", null)));
        }

        @PatchMapping("/{id}/status")
        public ResponseEntity<ApiResponse<OrderResponse>> updateStatus(
                        @PathVariable UUID id,
                        @RequestBody UpdateOrderStatusRequest request) {
                return orderService.updateStatus(id, request.status())
                                .map(order -> ResponseEntity.ok(new ApiResponse<>(
                                                true,
                                                "Order status updated",
                                                order)))
                                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                                                .body(new ApiResponse<>(false, "Order not found", null)));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable UUID id) {
                boolean deleted = orderService.deleteById(id);
                if (!deleted) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                        .body(new ApiResponse<>(
                                                        false,
                                                        "Order not found",
                                                        null));
                }
                return ResponseEntity.ok(new ApiResponse<>(
                                true,
                                "Order deleted",
                                null));
        }
}
