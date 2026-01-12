package com.example.user_service_client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record UserRegisterDto (
        @NotBlank(message = "Username is required")
        String username,
        @Email(message = "E-mail must be valid")
        @NotBlank(message = "E-mail is required")
        String email,
        @Size(min = 6,message = "Password must be at least 6 character")
        String password,
        @NotBlank(message = "This field must be filled")
        String address,
        @NotBlank(message = "This field must be filled")
        String gender,
        @DateTimeFormat
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate dob,
        @NotBlank
        @Size(min = 11 ,max =  11 ,message = "Phone number must be 11 character")
        String phone_number,
        @NotBlank(message = "This field must be filled")
        String full_name
){}
