package com.example.user_service_client.client;


import com.example.user_service_client.dto.AuthResponseDto;
import com.example.user_service_client.dto.UserLoginDto;
import com.example.user_service_client.dto.UserRegisterDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "user-service-app",
        contextId = "authClient",
        path = "/auth",
        url = "http://api-gateway:8080"
)
public interface AuthServiceClient {

    @PostMapping("/register")
    AuthResponseDto register(@RequestBody UserRegisterDto dto);

    @PostMapping("/login")
    AuthResponseDto login(@RequestBody UserLoginDto dto);
}