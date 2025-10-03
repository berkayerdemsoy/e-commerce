package com.example.user_service.dto;

import java.util.Date;

public record UserInformationDto(
        String address,
        String gender,
        Date dob,
        String phone_number,
        String full_name
) {
}
