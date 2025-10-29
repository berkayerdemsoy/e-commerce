package com.example.payment_service_app.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IdempotencyException.class)
    public ResponseEntity<?> handleIdempotency(IdempotencyException ex) {
        // log bağlamı ekleyin
        LoggerFactory.getLogger(getClass()).info("Idempotency exception: paymentId={}, status={}",
                ex.getPaymentId(), ex.getStatus());
        log.info("Idempotency exception: paymentId={}, status={}",
                ex.getPaymentId(), ex.getStatus());
        Object paymentId = ex.getPaymentId() != null ? ex.getPaymentId() : null;
        String status = ex.getStatus() != null ? ex.getStatus().name() : null;

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResp("IDEMPOTENT_REQUEST", ex.getMessage(), paymentId, status));
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResp("NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleConflict(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResp("CONFLICT", ex.getMessage()));
    }

    @ExceptionHandler(PaymentProcessingException.class)
    public ResponseEntity<?> handleProcessing(PaymentProcessingException ex) {
        log.error("Payment processing error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResp("PAYMENT_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        log.error("UNHANDLED EXCEPTION - Type: {}, Message: {}",
                ex.getClass().getName(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResp("INTERNAL_ERROR", "Beklenmeyen hata"));
    }

    // ErrorResp DTO
    public static class ErrorResp {
        public String code;
        public String message;
        public Object paymentId;
        public Object status;
        public ErrorResp(String code, String message) { this(code, message, null, null); }
        public ErrorResp(String code, String message, Object paymentId, Object status) {
            this.code = code; this.message = message; this.paymentId = paymentId; this.status = status;
        }
    }
}
