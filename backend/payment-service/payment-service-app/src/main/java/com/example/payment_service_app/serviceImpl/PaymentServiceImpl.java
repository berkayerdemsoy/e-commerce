package com.example.payment_service_app.serviceImpl;

import com.example.payment_service_app.config.KafkaProducerService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OutboxRepository outboxRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentProcessor paymentProcessor;
    private final KafkaProducerService kafkaProducerService;

    @Transactional
    @Override
    public PaymentResponse startPayment(PaymentRequest request) {
        log.info("=== PAYMENT START === userId={}, cartId={}, amount={}, idempotencyKey={}",
                request.getUserId(), request.getCartId(), request.getAmount(), request.getIdempotencyKey());

//        paymentRepository.findByIdempotencyKey(request.getIdempotencyKey())
//                .ifPresent(existing -> {
//                    throw new IdempotencyException(existing.getPaymentId(),existing.getStatus());
//                });
        try {
            paymentRepository.findByIdempotencyKey(request.getIdempotencyKey())
                    .ifPresent(existing -> {
                        log.warn("Duplicate payment detected: paymentId={}", existing.getPaymentId());
                        throw new IdempotencyException(existing.getPaymentId(), existing.getStatus());
                    });
        } catch (IdempotencyException e) {
            log.error("IdempotencyException thrown", e);
            throw e;
        } catch (Exception e) {
            log.error("Error during idempotency check", e);
            throw new PaymentProcessingException("Idempotency check failed", e);
        }

        UUID paymentId = UUID.randomUUID();
        log.info("Generated paymentId: {}", paymentId);
        Payment payment = Payment.builder()
                .paymentId(paymentId)
                .userId(request.getUserId())
                .cartId(request.getCartId())
                .amount(request.getAmount())
                .status(PaymentStatus.PENDING)
                .idempotencyKey(request.getIdempotencyKey())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
//        paymentRepository.save(payment);

        try {
            paymentRepository.save(payment);
            log.info("Payment saved: {}", paymentId);
        } catch (Exception e) {
            log.error("Failed to save payment: {}", paymentId, e);
            throw new PaymentProcessingException("Payment kayıt hatası", e);
        }
//        OutboxMessage init = OutboxMessage.builder()
//                .aggregateType("PAYMENT")
//                .aggregateId(paymentId.toString())
//                .type("payment.initiated")
//                .payload("{\"paymentId\":\"" + paymentId + "\"}")
//                .processed(false)
//                .createdAt(LocalDateTime.now())
//                .build();
//        outboxRepository.save(init);

        // Outbox kayıt
        try {
            OutboxMessage init = OutboxMessage.builder()
                    .aggregateType("PAYMENT")
                    .aggregateId(paymentId.toString())
                    .type("payment.initiated")
                    .payload("{\"paymentId\":\"" + paymentId + "\"}")
                    .processed(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            outboxRepository.save(init);
            log.info("Outbox init message saved");
        } catch (Exception e) {
            log.error("Failed to save outbox init", e);
        }

//        PaymentResult result;
//        try{
//            result = paymentProcessor.process(payment);
//        }catch (Exception e){
//            throw new PaymentProcessingException("Odeme isleminde hata!",e);
//        }

        PaymentResult result;
        try {
            log.info("Processing payment: {}", paymentId);
            result = paymentProcessor.process(payment);
            log.info("Payment processed: success={}", result.isSuccess());
        } catch (Exception e) {
            log.error("Payment processor failed: {}", paymentId, e);
            throw new PaymentProcessingException("Ödeme işleminde hata!", e);
        }


//        payment.setStatus(result.isSuccess() ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
//        payment.setProviderResponse(result.getProviderResponse());
//        payment.setUpdatedAt(LocalDateTime.now());
//        paymentRepository.save(payment);

        // Status güncelleme
        try {
            payment.setStatus(result.isSuccess() ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
            payment.setProviderResponse(result.getProviderResponse());
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);
            log.info("Payment status updated: {} -> {}", paymentId, payment.getStatus());
        } catch (Exception e) {
            log.error("Failed to update payment status", e);
            throw new PaymentProcessingException("Status güncelleme hatası", e);
        }

//        OutboxMessage resultOutbox = OutboxMessage.builder()
//                .aggregateType("PAYMENT")
//                .aggregateId(paymentId.toString())
//                .type(result.isSuccess() ? "payment.succeeded" : "payment.failed")
//                .payload(buildPayload(payment))
//                .processed(false)
//                .createdAt(LocalDateTime.now())
//                .build();
//        outboxRepository.save(resultOutbox);
//        return paymentMapper.toResponse(payment);

        try {
            OutboxMessage resultOutbox = OutboxMessage.builder()
                    .aggregateType("PAYMENT")
                    .aggregateId(paymentId.toString())
                    .type(result.isSuccess() ? "payment.succeeded" : "payment.failed")
                    .payload(buildPayload(payment))
                    .processed(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            outboxRepository.save(resultOutbox);
            kafkaProducerService.sendMessage("payment-topic",resultOutbox.getPayload());
            log.info("Outbox result message saved");
        } catch (Exception e) {
            log.error("Failed to save outbox result", e);
        }


        log.info("=== PAYMENT COMPLETE === paymentId={}, status={}", paymentId, payment.getStatus());
        return paymentMapper.toResponse(payment);
    }


    private String buildPayload(Payment p) {
        return String.format("{\"paymentId\":\"%s\",\"userId\":%d,\"cartId\":%d,\"amount\":%s,\"status\":\"%s\",\"eventType\":\"%s\"}",
                p.getPaymentId(), p.getUserId(), p.getCartId(), p.getAmount(), p.getStatus().name() , p.getStatus() == PaymentStatus.SUCCESS ? "payment.succeeded" : "payment.failed" );
    }
}
