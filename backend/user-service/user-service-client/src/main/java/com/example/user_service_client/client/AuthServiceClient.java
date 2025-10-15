package com.example.user_service_client.client;


import com.example.user_service_client.dto.UserLoginDto;
import com.example.user_service_client.dto.UserRegisterDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "user-service-app",
        path = "/auth"
)
public interface AuthServiceClient {

    @PostMapping("/register")
    String register(@RequestBody UserRegisterDto dto);

    @PostMapping("/login")
    String login(@RequestBody UserLoginDto dto);
}