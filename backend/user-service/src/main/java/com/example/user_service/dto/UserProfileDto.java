package com.example.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

public record UserProfileDto(

        @NotBlank
        String address,
        @NotBlank
        String gender,
        @DateTimeFormat
        LocalDate dob,
        @NotBlank
        String phone_number,
        @NotBlank
        String full_name
) {
}
