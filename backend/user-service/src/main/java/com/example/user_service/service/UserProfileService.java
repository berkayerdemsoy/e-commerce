package com.example.user_service.service;

import com.example.user_service.dto.UserProfileDto;
import com.example.user_service.entity.UserProfile;

public interface UserProfileService {
    UserProfileDto getUserProfileById(Long id);
    UserProfileDto updateUserProfile(Long id , UserProfileDto userProfileDto);
}
