package com.example.payment_service_app.controller;

import com.example.payment_service_app.service.PaymentService;
import com.example.payment_service_client.dto.PaymentRequest;
import com.example.payment_service_client.dto.PaymentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> startPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse resp = paymentService.startPayment(request);
        return ResponseEntity.status(201).body(resp);

    }
}
