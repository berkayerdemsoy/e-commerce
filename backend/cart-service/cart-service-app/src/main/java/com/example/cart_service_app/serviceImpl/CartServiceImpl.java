package com.example.cart_service_app.serviceImpl;

import com.example.cart_service_app.entity.Cart;
import com.example.cart_service_app.entity.CartItem;
import com.example.cart_service_app.exception.NotFoundException;
import com.example.cart_service_app.repository.CartRepository;
import com.example.cart_service_app.service.CartService;
import com.example.shop_service_client.client.ProductClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductClient productClient;
    private static final long CART_TTL_MINUTES = 30;

    @Override
    public Cart getCart(String userId) {
        Cart cart = cartRepository.getCart(userId);
        if (cart == null) {
            cart = new Cart(userId, new ArrayList<>(), LocalDateTime.now());
            cartRepository.saveCart(cart, CART_TTL_MINUTES);
        }
        return cart;
    }
    @Retryable(
            value = { RedisConnectionFailureException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    @Override
    public Cart addItem(String userId, CartItem item) {

        try {
            productClient.getProductById(Long.parseLong(item.getProductId()));
        }catch (FeignException.NotFound e){
            throw new NotFoundException("Product not found:" + item.getProductId());

        }

        Cart cart = getCart(userId);
        cart.getItems().stream()
                .filter(ci -> ci.getProductId().equals(item.getProductId()))
                .findFirst()
                .ifPresentOrElse(
                        existingItem -> existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity()),
                        () -> cart.getItems().add(item)
                );
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.saveCart(cart, CART_TTL_MINUTES);
        return cart;
    }


    @Override
    public Cart updateQuantity(String userId, String productId, int quantity) {
        Cart cart = getCart(userId);

        cart.getItems().removeIf(ci -> ci.getProductId().equals(productId) && quantity == 0);

        cart.getItems().stream()
                .filter(ci -> ci.getProductId().equals(productId))
                .findFirst()
                .ifPresent(ci -> ci.setQuantity(quantity));

        cart.setUpdatedAt(LocalDateTime.now());
        saveOrClear(userId, cart);
        return cart;
    }

    @Override
    public Cart removeItem(String userId, String productId) {
        Cart cart = getCart(userId);
        cart.getItems().removeIf(ci -> ci.getProductId().equals(productId));
        cart.setUpdatedAt(LocalDateTime.now());
        saveOrClear(userId, cart);
        return cart;
    }

    @Override
    public void clearCart(String userId) {
        cartRepository.deleteCart(userId);
    }

    private void saveOrClear(String userId, Cart cart) {
        if (cart.getItems().isEmpty()) {
            cartRepository.deleteCart(userId);
        } else {
            cartRepository.saveCart(cart, CART_TTL_MINUTES);
        }
    }
}
