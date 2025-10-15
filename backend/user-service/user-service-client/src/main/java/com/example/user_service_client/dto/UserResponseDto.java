package com.example.user_service_client.dto;

import java.util.Set;

public record UserResponseDto(
        Long id,
        String username,
        String email,
        Set<String> roles
) {
}
