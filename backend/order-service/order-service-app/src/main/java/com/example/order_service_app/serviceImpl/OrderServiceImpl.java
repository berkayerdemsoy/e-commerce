package com.example.order_service_app.serviceImpl;

import com.example.order_service_app.entity.Order;
import com.example.order_service_app.mapper.OrderMapper;
import com.example.order_service_app.repository.OrderRepository;
import com.example.order_service_app.service.OrderService;
import com.example.order_service_client.dto.OrderRequest;
import com.example.order_service_client.dto.OrderResponse;
import com.example.order_service_client.enums.StatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;


    @Override
    public OrderResponse findOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        return orderMapper.toDto(order);
    }

    @Override
    public Void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
        return null;
    }

    @Override
    public OrderResponse updateOrderById(Long id, OrderRequest orderRequest) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setTotalAmount(orderRequest.getTotalAmount());
         Order updated = orderRepository.save(order);
         return orderMapper.toDto(updated);
    }

    @Override
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(orderMapper::toDto);
    }

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
       Order order = Order.builder()
               .status(StatusType.PENDING)
               .userId(orderRequest.getUserId())
               .totalAmount(orderRequest.getTotalAmount())
               .build();
       Order saved = orderRepository.save(order);
       return orderMapper.toDto(saved);
    }
}
