package com.example.user_service_app.controller;

import com.example.user_service_client.dto.AuthResponseDto;
import com.example.user_service_client.dto.UserLoginDto;
import com.example.user_service_client.dto.UserRegisterDto;
import com.example.user_service_client.dto.UserResponseDto;
import com.example.user_service_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        AuthResponseDto response = userService.register(userRegisterDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        AuthResponseDto response = userService.login(userLoginDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Refresh token ile yeni access token al.
     * Body: { "refreshToken": "..." }
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        AuthResponseDto response = userService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }

    /**
     * Mevcut kullanıcının bilgilerini JWT token'dan çıkararak döndür.
     * Angular'da login sonrası profil bilgisi çekmek için kullanılır.
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponseDto user = userService.getCurrentUser(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }
}
