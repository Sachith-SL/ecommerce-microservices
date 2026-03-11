package com.sachith.product_service.service.impl;

import com.sachith.product_service.dto.CreateProductRequest;
import com.sachith.product_service.dto.ProductResponse;
import com.sachith.product_service.model.Product;
import com.sachith.product_service.model.ProductType;
import com.sachith.product_service.model.UnitOfMeasure;
import com.sachith.product_service.repository.ProductRepository;
import com.sachith.product_service.service.ProductService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse create(CreateProductRequest request) {
        Product product = new Product();
        product.setSku(request.sku());
        product.setName(request.name());
        product.setDescription(request.description());
        product.setCategory(request.category());
        product.setProductType(ProductType.valueOf(request.productType().toUpperCase()));
        product.setUnitOfMeasure(UnitOfMeasure.valueOf(request.unitOfMeasure().toUpperCase()));
        product.setUnitPrice(request.unitPrice());
        product.setCurrency(request.currency());
        product.setActive(request.active());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        Product saved = productRepository.save(product);
        return toProductResponse(saved);
    }

    @Override
    public List<ProductResponse> getAll() {
        return productRepository.findAll()
                .stream()
                .map(this::toProductResponse)
                .toList();
    }

    @Override
    public ProductResponse getById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return toProductResponse(product);
    }

    @Override
    public void delete(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }

    private ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getProductType() != null ? product.getProductType().name() : null,
                product.getUnitOfMeasure() != null ? product.getUnitOfMeasure().name() : null,
                product.getUnitPrice(),
                product.getCurrency(),
                product.getActive(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
