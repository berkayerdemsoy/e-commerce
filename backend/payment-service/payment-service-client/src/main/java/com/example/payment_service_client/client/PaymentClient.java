package com.example.payment_service_client.client;

import com.example.payment_service_client.dto.PaymentRequest;
import com.example.payment_service_client.dto.PaymentResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "payment-service",
        path = "/api/payment",
        url = "http://api-gateway:8080"
)
public interface PaymentClient {

    @PostMapping
    PaymentResponse startPayment(@Valid @RequestBody PaymentRequest request);
}
