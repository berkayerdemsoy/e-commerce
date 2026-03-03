package com.example.user_service_client.dto;

import java.util.Set;

public record AuthResponseDto(
        String token,
        Long userId,
        String username,
        String email,
        Set<String> roles
) {
    /**
     * Backward-compatible factory — sadece token döndürmek gerektiğinde.
     */
    public static AuthResponseDto ofToken(String token) {
        return new AuthResponseDto(token, null, null, null, null);
    }
}
