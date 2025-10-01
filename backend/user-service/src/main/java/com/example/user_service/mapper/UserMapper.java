package com.example.user_service.mapper;

import com.example.user_service.dto.UserRegisterRequestDto;
import com.example.user_service.dto.UserResponseDto;
import com.example.user_service.dto.UserUpdateRequestDto;
import com.example.user_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(User user);
    User toEntityFromRegister(UserRegisterRequestDto userRegisterRequestDto);
    User toEntityFromUpdateRequest(UserUpdateRequestDto userUpdateRequestDto , @MappingTarget User user);
}
