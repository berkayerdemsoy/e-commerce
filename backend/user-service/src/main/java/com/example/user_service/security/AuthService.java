package com.example.user_service.security;

import com.example.user_service.dto.AuthResponse;
import com.example.user_service.entity.User;
import com.example.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.awt.geom.RectangularShape;

@Component("authUtils")
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public Long getUserId(Authentication authentication){
        return userRepository.findByUsername(authentication.getName())
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
    }
}
