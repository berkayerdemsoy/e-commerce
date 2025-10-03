package com.example.user_service.service;

import com.example.user_service.dto.UserInformationDto;

public interface UserInformationService {
    UserInformationDto getUserInformationById(Long userId);
    UserInformationDto updateUserInformation(Long userId,UserInformationDto dto);
}
