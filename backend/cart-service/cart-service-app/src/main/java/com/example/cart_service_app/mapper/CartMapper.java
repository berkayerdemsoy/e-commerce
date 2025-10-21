package com.example.cart_service_app.mapper;

import com.example.cart_service_app.entity.Cart;
import com.example.cart_service_app.entity.CartItem;
import com.example.cart_service_client.dto.CartDTO;
import com.example.cart_service_client.dto.CartItemDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartDTO toDto(Cart cart);

    CartItemDTO toDto(CartItem item);

    CartItem toEntity(CartItemDTO dto);

    List<CartItemDTO> toDtoList(List<CartItem> items);

    List<CartItem> toEntityList(List<CartItemDTO> items);
}
