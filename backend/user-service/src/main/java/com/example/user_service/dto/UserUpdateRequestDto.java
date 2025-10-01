package com.example.user_service.dto;

import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequestDto(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Password is Required")
        String password
) {
}
