package com.example.user_service.mapper;

import com.example.user_service.dto.UserInformationDto;
import com.example.user_service.entity.UserInformation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserInformationMapper {
    UserInformation toEntity(UserInformationDto dto);
    UserInformationDto toDto(UserInformation userInformation);
}
