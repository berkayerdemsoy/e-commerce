package com.example.cart_service_app.controller;

import com.example.cart_service_app.entity.Cart;
import com.example.cart_service_app.entity.CartItem;
import com.example.cart_service_app.mapper.CartMapper;
import com.example.cart_service_app.service.CartService;
import com.example.cart_service_client.dto.CartDTO;
import com.example.cart_service_client.dto.CartItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Recover;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;


    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable("userId") String userId) {
        Cart cart = cartService.getCart(userId);
        return ResponseEntity.ok(cartMapper.toDto(cart));
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<CartDTO> addItem(@PathVariable("userId") String userId,
                                           @RequestBody CartItemDTO itemDto) {
        CartItem item = cartMapper.toEntity(itemDto);
        Cart cart = cartService.addItem(userId, item);
        return ResponseEntity.ok(cartMapper.toDto(cart));
    }

    @PutMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartDTO> updateQuantity(@PathVariable("userId") String userId,
                                                  @PathVariable("productId") String productId,
                                                  @RequestParam int quantity) {
        Cart cart = cartService.updateQuantity(userId, productId, quantity);
        return ResponseEntity.ok(cartMapper.toDto(cart));
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartDTO> removeItem(@PathVariable("userId") String userId,
                                              @PathVariable("productId") String productId) {
        Cart cart = cartService.removeItem(userId, productId);
        return ResponseEntity.ok(cartMapper.toDto(cart));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable("userId") String userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }


    @Recover
    public Cart recover(RedisConnectionFailureException e, String userId) {
        // Redis hiç bağlanamazsa fallback davranışı
        return new Cart(userId, new ArrayList<>(), LocalDateTime.now());
    }
}
