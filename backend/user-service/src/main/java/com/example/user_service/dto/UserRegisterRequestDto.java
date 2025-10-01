package com.example.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequestDto(
        @NotBlank(message = "Username is required")
        String username,
        @Email(message = "E-mail must be valid")
        @NotBlank(message = "E-mail is required")
        String email,
        @Size(min = 6,message = "Password must be at least 6 character")
        String password

) {
}
