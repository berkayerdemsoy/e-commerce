package com.example.order_service_app.mapper;

import com.example.order_service_app.entity.Order;
import com.example.order_service_client.dto.OrderResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toDto(Order order);
}
