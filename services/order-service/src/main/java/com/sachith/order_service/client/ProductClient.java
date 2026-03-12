package com.sachith.order_service.client;

import com.sachith.order_service.dto.InventoryRequest;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;
@Service
public class ProductClient {

    private final RestClient restClient;

    public ProductClient(@LoadBalanced RestClient.Builder builder) {
        this.restClient = builder.build();;
    }

    public String checkProduct(UUID productId) {

        return restClient.get()
                .uri("http://product-service/api/scn/v1/product/check/{productId}",productId)
                .retrieve()
                .body(String.class);
    }
}





