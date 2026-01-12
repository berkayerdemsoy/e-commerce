package com.example.payment_service_app.processor;

import com.example.payment_service_app.entity.Payment;

public interface PaymentProcessor {
    PaymentResult process(Payment payment);

}