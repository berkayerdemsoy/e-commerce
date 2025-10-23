package com.example.payment_service_app.mapper;

import com.example.payment_service_app.entity.Payment;
import com.example.payment_service_client.dto.PaymentResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentResponse toResponse(Payment payment);
}
