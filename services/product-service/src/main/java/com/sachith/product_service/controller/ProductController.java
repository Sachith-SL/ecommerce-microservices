package com.sachith.product_service.controller;

import com.sachith.product_service.dto.ApiResponse;
import com.sachith.product_service.dto.CreateProductRequest;
import com.sachith.product_service.dto.ProductResponse;
import com.sachith.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/scn/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @Value("${server.port}")
    private String port;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> create(@RequestBody CreateProductRequest request) {
        ProductResponse saved = productService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        "Product created",
                        saved));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAll() {
        List<ProductResponse> products = productService.getAll();
        return ResponseEntity
                .ok(new ApiResponse<>(
                        true,
                        "Products fetched",
                        products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getById(@PathVariable UUID id) {
        ProductResponse product = productService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Product fetched", product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable UUID id) {
        productService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Product deleted", null));
    }

    @GetMapping("/check/{productId}")
    public String checkInventory(@PathVariable UUID productId) {
        ProductResponse product = productService.getById(productId);
        return "Inventory response from port: " + port + " for product: " + product.sku();
    }


}
