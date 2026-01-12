package com.example.payment_service_app.processor;

import com.example.payment_service_app.entity.Payment;
import org.springframework.stereotype.Service;

@Service
public class DefaultPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentResult process(Payment payment) {
        // deterministic success for now — kolayca gerçek gateway eklersin
        boolean success = true;
        String resp = "simulated-ok";
        return PaymentResult.success(resp);
    }
}