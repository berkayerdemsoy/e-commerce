package com.example.user_service.service;

import com.example.user_service.dto.UserLoginDto;
import com.example.user_service.dto.UserRegisterDto;
import com.example.user_service.dto.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    String register(UserRegisterDto userRegisterDto);
    String login(UserLoginDto userLoginDto);
    Page<UserResponseDto> getAllUsers(Pageable pageable);
    UserResponseDto getUserById(Long id);
    Void deleteUserById(Long id);
    UserResponseDto getUserByUsername(String username);
    UserResponseDto updateUserById(Long id, UserRegisterDto userRegisterDto);
}
