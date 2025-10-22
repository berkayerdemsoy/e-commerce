package com.example.order_service_app.service;


import com.example.order_service_client.dto.OrderRequest;
import com.example.order_service_client.dto.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
 OrderResponse findOrderById(Long id);
 Void deleteOrderById(Long id);
 OrderResponse updateOrderById(Long id , OrderRequest orderRequest);
 Page<OrderResponse> getAllOrders(Pageable pageable);
 OrderResponse createOrder(OrderRequest orderRequest);
}
