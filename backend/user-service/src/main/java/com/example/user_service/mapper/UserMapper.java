package com.example.user_service.mapper;

import com.example.user_service.dto.UserResponseDto;
import com.example.user_service.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(User user);
}
