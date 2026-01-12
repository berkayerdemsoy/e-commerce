package com.example.cart_service_client.client;

import com.example.cart_service_client.dto.CartDTO;
import com.example.cart_service_client.dto.CartItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "cart-service",
        contextId = "cartClient",
        path = "/api/cart",
        url = "http://api-gateway:8080"
)
public interface CartClient {

    @GetMapping("/{userId}")
    CartDTO getCart(@PathVariable("userId") String userId);

    @PostMapping("/{userId}/items")
    CartDTO addItem(@PathVariable("userId") String userId,
                    @RequestBody CartItemDTO item);

    @PutMapping("/{userId}/items/{productId}")
    CartDTO updateQuantity(@PathVariable("userId") String userId,
                           @PathVariable("productId") String productId,
                           @RequestParam("quantity") int quantity);

    @DeleteMapping("/{userId}/items/{productId}")
    CartDTO removeItem(@PathVariable("userId") String userId,
                       @PathVariable("productId") String productId);

    @DeleteMapping("/{userId}")
    void clearCart(@PathVariable("userId") String userId);
}
