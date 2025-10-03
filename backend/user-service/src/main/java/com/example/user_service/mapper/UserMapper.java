package com.example.user_service.mapper;

import com.example.user_service.dto.UserRegisterRequestDto;
import com.example.user_service.dto.UserResponseDto;
import com.example.user_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(User user);
    User toEntityFromRegister(UserRegisterRequestDto userRegisterRequestDto);
//    User toEntityFromUpdateRequest(UserUpdateRequestDto userUpdateRequestDto , @MappingTarget User user);
}
