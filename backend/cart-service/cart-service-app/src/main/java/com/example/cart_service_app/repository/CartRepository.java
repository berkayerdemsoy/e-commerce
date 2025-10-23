package com.example.cart_service_app.repository;

import com.example.cart_service_app.entity.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class CartRepository {

    @Qualifier("cartRedisTemplate")
    private final RedisTemplate<String, Cart> redisTemplate;

    private String getKey(String userId) {
        return "cart:" + userId;
    }

    public void saveCart(Cart cart , long ttlMinutes){
        redisTemplate.opsForValue().set(getKey(cart.getUserId()), cart, ttlMinutes , TimeUnit.MINUTES);
    }

    public Cart getCart(String userId){
        return redisTemplate.opsForValue().get(getKey(userId));
    }

    public void deleteCart(String userId){
        redisTemplate.delete(getKey(userId));
    }
}

