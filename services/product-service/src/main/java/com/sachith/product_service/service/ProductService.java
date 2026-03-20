package com.sachith.product_service.service;

import com.sachith.product_service.dto.CreateProductRequest;
import com.sachith.product_service.dto.ProductResponse;
import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductResponse create(CreateProductRequest request);

    List<ProductResponse> getAll();

    List<ProductResponse> getActiveProducts();

    ProductResponse getById(UUID id);

    void deactivate(UUID id);

    void activate(UUID id);

    void delete(UUID id);
}
