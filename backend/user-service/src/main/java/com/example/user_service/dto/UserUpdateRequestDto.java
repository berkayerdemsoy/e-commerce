package com.example.user_service.dto;

import com.example.user_service.entity.Role;

import java.util.Set;

public record UserUpdateRequestDto (
        String email,
        String password,
        Set<Role> roles
) {
}
