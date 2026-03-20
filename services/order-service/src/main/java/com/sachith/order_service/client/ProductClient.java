package com.sachith.order_service.client;

import com.sachith.order_service.dto.ApiResponse;
import com.sachith.order_service.dto.ProductResponse;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;
@Service
public class ProductClient {

    private final RestClient restClient;

    public ProductClient(@LoadBalanced RestClient.Builder builder) {
        this.restClient = builder.build();;
    }

    public ApiResponse<ProductResponse> checkProduct(UUID productId) {

        return restClient.get()
                .uri("http://product-service/api/scn/v1/product/{productId}",productId)
                .retrieve()
                .body(new ParameterizedTypeReference<ApiResponse<ProductResponse>>() {});
    }
}





