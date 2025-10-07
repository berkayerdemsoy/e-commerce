package com.example.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter @Setter
public class ErrorResponseDto {
    private String errorCode;
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private String path;
}
