package com.sachith.product_service.service;

import com.sachith.product_service.dto.CreateProductRequest;
import com.sachith.product_service.dto.ProductResponse;
import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductResponse create(CreateProductRequest request);

    List<ProductResponse> getAll();

    ProductResponse getById(UUID id);

    void delete(UUID id);
}
