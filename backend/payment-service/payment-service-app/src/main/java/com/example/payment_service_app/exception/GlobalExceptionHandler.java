package com.example.payment_service_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IdempotencyException.class)
    public ResponseEntity<?> handleIdempotency(IdempotencyException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResp("IDEMPOTENT_REQUEST", ex.getMessage(),
                        ex.getPaymentId(), ex.getStatus().name()));
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
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResp("PAYMENT_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
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
