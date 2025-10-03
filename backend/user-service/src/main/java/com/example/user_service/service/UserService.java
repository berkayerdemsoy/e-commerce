package com.example.user_service.service;


import com.example.user_service.dto.*;
import com.example.user_service.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    void deleteUserById(Long id);
    Optional<UserResponseDto> getUserByUsername(String username);
    AuthResponse registerUser(UserRegisterRequestDto dto);
    AuthResponse loginUser(UserLoginRequestDto dto);
    Page<UserResponseDto> getALlUsers(Pageable pageable);
    UserResponseDto getUserById(Long id);
    UserResponseDto updateUserById(Long id, UserUpdateRequestDto userUpdateRequestDto);
}
