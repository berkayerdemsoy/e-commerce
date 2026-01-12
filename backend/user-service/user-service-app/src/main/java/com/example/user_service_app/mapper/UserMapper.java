package com.example.user_service_app.mapper;

import com.example.user_service_client.dto.UserResponseDto;
import com.example.user_service_app.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(User user);
}
