package com.example.warehouse_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponseDto {
    private String errorCode;
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private String path;
}
