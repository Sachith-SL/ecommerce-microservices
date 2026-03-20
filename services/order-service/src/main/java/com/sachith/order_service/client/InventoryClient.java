package com.sachith.order_service.client;

import com.sachith.order_service.dto.ApiResponse;
import com.sachith.order_service.dto.InventoryResponse;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
public class InventoryClient {

    private final RestClient restClient;

    public InventoryClient(@LoadBalanced RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public ApiResponse<InventoryResponse> checkInventory(UUID productId) {

        return restClient.get()
                .uri("http://inventory-service/api/scn/v1/inventory/check/{productId}", productId)
                .retrieve()
                .body(new ParameterizedTypeReference<ApiResponse<InventoryResponse>>() {});
    }
}
