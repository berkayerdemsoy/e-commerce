package com.example.warehouse_service_app.exception;

public class InvalidStockChangeException extends RuntimeException {
    public InvalidStockChangeException(String message) {
        super(message);
    }
}
