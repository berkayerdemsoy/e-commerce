package com.example.user_service_app.service;

import com.example.user_service_client.dto.UserLoginDto;
import com.example.user_service_client.dto.UserRegisterDto;
import com.example.user_service_client.dto.UserResponseDto;
import com.example.user_service_client.dto.UserRoleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    String register(UserRegisterDto userRegisterDto);
    String login(UserLoginDto userLoginDto);
    Page<UserResponseDto> getAllUsers(Pageable pageable);
    UserResponseDto getUserById(Long id);
    Void deleteUserById(Long id);
    UserResponseDto getUserByUsername(String username);
    UserResponseDto updateUserById(Long id, UserRegisterDto userRegisterDto);
    List<UserRoleResponse> getUsersByRole(String role);

}
