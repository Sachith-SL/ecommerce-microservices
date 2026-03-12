package com.sachith.order_service.client;

import com.sachith.order_service.dto.InventoryRequest;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
public class InventoryClient {

    private final RestClient restClient;

    public InventoryClient(@LoadBalanced RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public String checkInventory(UUID productId) {

        return restClient.get()
                .uri("http://inventory-service/api/scn/v1/inventory/check/{productId}", productId)
                .retrieve()
                .body(String.class);
    }
}
