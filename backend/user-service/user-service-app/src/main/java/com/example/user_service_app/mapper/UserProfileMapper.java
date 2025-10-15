package com.example.user_service_app.mapper;

import com.example.user_service_client.dto.UserProfileDto;
import com.example.user_service_app.entity.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileDto toDto(UserProfile userProfile);
    UserProfile toEntity(UserProfileDto userProfileDto);
}
