package com.example.user_service.exception;

import com.example.user_service.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler  {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception
            , HttpServletRequest request){

        ErrorResponseDto error = new ErrorResponseDto(
                "USER_NOT_FOUND",
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
    public ResponseEntity<Object> handleUserAlreadyExistsException(AlreadyExistsException exception,
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

    @ExceptionHandler({InvalidCredentialsException.class})
    public ResponseEntity<Object> handleInvalidCredentialsHandler(InvalidCredentialsException exception,
                                                                  HttpServletRequest request){
        ErrorResponseDto error = new ErrorResponseDto(
                "INVALID_CREDENTIALS",
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
    public ResponseEntity<Object> handleRuntimeException(RuntimeException exception,
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
