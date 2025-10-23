package com.example.payment_service_app.serviceImpl;

import com.example.payment_service_app.entity.OutboxMessage;
import com.example.payment_service_app.entity.Payment;
import com.example.payment_service_app.exception.IdempotencyException;
import com.example.payment_service_app.exception.PaymentProcessingException;
import com.example.payment_service_app.mapper.PaymentMapper;
import com.example.payment_service_app.processor.PaymentProcessor;
import com.example.payment_service_app.processor.PaymentResult;
import com.example.payment_service_app.repository.OutboxRepository;
import com.example.payment_service_app.repository.PaymentRepository;
import com.example.payment_service_app.service.PaymentService;
import com.example.payment_service_client.dto.PaymentRequest;
import com.example.payment_service_client.dto.PaymentResponse;
import com.example.payment_service_client.enums.PaymentStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OutboxRepository outboxRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentProcessor paymentProcessor;

    @Transactional
    @Override
    public PaymentResponse startPayment(PaymentRequest request) {

        paymentRepository.findByIdempotencyKey(request.getIdempotencyKey())
                .ifPresent(existing -> {
                    throw new IdempotencyException(existing.getPaymentId(),existing.getStatus());
                });

        UUID paymentId = UUID.randomUUID();
        Payment payment = Payment.builder()
                .paymentId(paymentId)
                .userId(request.getUserId())
                .cartId(Long.parseLong(request.getCartId()))
                .amount(request.getAmount())
                .status(PaymentStatus.PENDING)
                .idempotencyKey(request.getIdempotencyKey())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        paymentRepository.save(payment);

        OutboxMessage init = OutboxMessage.builder()
                .aggregateType("PAYMENT")
                .aggregateId(paymentId.toString())
                .type("payment.initiated")
                .payload("{\"paymentId\":\"" + paymentId + "\"}")
                .processed(false)
                .createdAt(LocalDateTime.now())
                .build();
        outboxRepository.save(init);

        PaymentResult result;
        try{
            result = paymentProcessor.process(payment);
        }catch (Exception e){
            throw new PaymentProcessingException("Odeme isleminde hata!",e);
        }


        payment.setStatus(result.isSuccess() ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        payment.setProviderResponse(result.getProviderResponse());
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        OutboxMessage resultOutbox = OutboxMessage.builder()
                .aggregateType("PAYMENT")
                .aggregateId(paymentId.toString())
                .type(result.isSuccess() ? "payment.succeeded" : "payment.failed")
                .payload(buildPayload(payment))
                .processed(false)
                .createdAt(LocalDateTime.now())
                .build();
        outboxRepository.save(resultOutbox);
        return paymentMapper.toResponse(payment);

    }
    private String buildPayload(Payment p) {
        return String.format("{\"paymentId\":\"%s\",\"userId\":%d,\"cartId\":%d,\"amount\":%s,\"status\":\"%s\"}",
                p.getPaymentId(), p.getUserId(), p.getCartId(), p.getAmount(), p.getStatus().name());
    }
}
