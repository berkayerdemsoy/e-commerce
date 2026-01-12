package com.example.payment_service_app.service;

import com.example.payment_service_client.dto.PaymentRequest;
import com.example.payment_service_client.dto.PaymentResponse;

public interface PaymentService {
    PaymentResponse startPayment(PaymentRequest request);
}
