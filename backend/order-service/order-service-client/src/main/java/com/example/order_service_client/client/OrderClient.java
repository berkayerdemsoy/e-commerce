package com.example.order_service_client.client;

import com.example.order_service_client.dto.OrderRequest;
import com.example.order_service_client.dto.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "order-service",
        path = "/api/order",
        url = "http://api-gateway:8080"
)
public interface OrderClient {

    @GetMapping("/order/{id}")
    OrderResponse getById(@PathVariable("id") Long id);

    @PostMapping("/order/create")
    OrderResponse createOrder(@RequestBody OrderRequest orderRequest);

    @PutMapping("/order/{id}")
    OrderResponse updateOrder(@PathVariable("id") Long id,
                              @RequestBody OrderRequest orderRequest);

    @DeleteMapping("/order/{id}")
    void deleteById(@PathVariable("id") Long id);
}
