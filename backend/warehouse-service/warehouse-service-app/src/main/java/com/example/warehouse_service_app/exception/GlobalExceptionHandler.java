package com.example.warehouse_service_app.exception;

import com.example.warehouse_service_client.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> handlerNotFoundException(NotFoundException exception,
                                                           HttpServletRequest request){
        ErrorResponseDto error = new ErrorResponseDto(
                "NOT_FOUND",
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler({AlreadyExistsException.class})
    public ResponseEntity<Object> handlerAlreadyExists(AlreadyExistsException exception,
                                                       HttpServletRequest request){
        ErrorResponseDto error = new ErrorResponseDto(
                "ALREADY_EXISTS",
                exception.getMessage(),
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }
    @ExceptionHandler({InvalidStockChangeException.class})
    public ResponseEntity<Object> handlerInvalidStockChangeException(InvalidStockChangeException exception,
                                                       HttpServletRequest request){
        ErrorResponseDto error = new ErrorResponseDto(
                "BAD_REQUEST",
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handlerRuntimeException(RuntimeException exception,
                                                          HttpServletRequest request){
        ErrorResponseDto error = new ErrorResponseDto(
                "INTERNAL_SERVER_ERROR",
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
