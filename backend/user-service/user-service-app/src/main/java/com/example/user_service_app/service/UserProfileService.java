package com.example.user_service_app.service;

import com.example.user_service_client.dto.UserProfileDto;
import com.example.user_service_app.entity.UserProfile;

public interface UserProfileService {
    UserProfileDto getUserProfileById(Long id);
    UserProfileDto updateUserProfile(Long id , UserProfileDto userProfileDto);
}
