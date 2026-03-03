package com.example.user_service_app.service;

import com.example.user_service_client.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    AuthResponseDto register(UserRegisterDto userRegisterDto);
    AuthResponseDto login(UserLoginDto userLoginDto);
    AuthResponseDto refreshToken(String refreshToken);
    UserResponseDto getCurrentUser(String username);
    Page<UserResponseDto> getAllUsers(Pageable pageable);
    UserResponseDto getUserById(Long id);
    Void deleteUserById(Long id);
    UserResponseDto getUserByUsername(String username);
    UserResponseDto updateUserById(Long id, UserLoginDto userLoginDto);
    List<UserRoleResponse> getUsersByRole(String role);

}
