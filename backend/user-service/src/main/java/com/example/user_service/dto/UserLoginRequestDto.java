package com.example.user_service.dto;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDto(
        @NotBlank(message = "Username or E-mail is required")
        String usernameOrEmail,
        @NotBlank(message = "Password is required")
        String password
) {
}
