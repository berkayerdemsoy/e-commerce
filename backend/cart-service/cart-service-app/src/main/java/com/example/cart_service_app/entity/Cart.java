package com.example.cart_service_app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private String userId;
    private List<CartItem> items = new ArrayList<>();
    private LocalDateTime updatedAt;
}