package com.example.cart_service_app.service;

import com.example.cart_service_app.entity.Cart;
import com.example.cart_service_app.entity.CartItem;

public interface CartService {

    Cart getCart(String userId);

    Cart addItem(String userId, CartItem item);

    Cart updateQuantity(String userId, String productId, int quantity);

    Cart removeItem(String userId, String productId);

    void clearCart(String userId);
}
