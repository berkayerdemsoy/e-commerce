package com.example.order_service_app.serviceImpl;

import com.example.cart_service_client.client.CartClient;
import com.example.cart_service_client.dto.CartDTO;
import com.example.cart_service_client.dto.CartItemDTO;
import com.example.order_service_app.config.OrderEventPublisher;
import com.example.order_service_app.entity.Order;
import com.example.order_service_app.entity.OrderItem;
import com.example.order_service_app.mapper.OrderMapper;
import com.example.order_service_app.repository.OrderItemRepository;
import com.example.order_service_app.repository.OrderRepository;
import com.example.order_service_app.service.OrderService;
import com.example.order_service_client.dto.OrderEventDto;
import com.example.order_service_client.dto.OrderRequest;
import com.example.order_service_client.dto.OrderResponse;
import com.example.order_service_client.dto.PaymentEventDto;
import com.example.order_service_client.enums.StatusType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final CartClient cartClient;
    private final OrderEventPublisher orderEventPublisher;


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

    @Override
    public OrderResponse createOrderFromPayment(PaymentEventDto paymentEventDto) {
        log.info("Creating order from payment event: {}", paymentEventDto);

        CartDTO cart= cartClient.getCart(Long.toString(paymentEventDto.getCartId()));
        Order order = Order.builder()
                .userId(paymentEventDto.getUserId())
                .status(StatusType.PENDING)
                .totalAmount(paymentEventDto.getAmount())
                .build();
        order = orderRepository.save(order);

        Long orderId = order.getId();
        for (CartItemDTO item : cart.getItems()){
            OrderItem orderItem = OrderItem.builder()
                    .orderId(orderId)
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .priceAtPurchase(item.getPrice())
                    .build();
            orderItemRepository.save(orderItem);
        }
        order.setStatus(StatusType.CONFIRMED);
        orderRepository.save(order);

        OrderEventDto orderEvent = OrderEventDto.builder()
                .orderId(orderId)
                .userId(paymentEventDto.getUserId())
                .totalAmount(BigDecimal.valueOf(paymentEventDto.getAmount()))
                .eventType("order.confirmed")
                .build();

        orderEventPublisher.publishOrderConfirmed(orderEvent);


        log.info("Order created successfully: orderId={}", orderId);

        return orderMapper.toDto(order);
    }


}
