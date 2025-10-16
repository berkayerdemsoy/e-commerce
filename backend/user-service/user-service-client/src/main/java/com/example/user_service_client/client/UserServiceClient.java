package com.example.user_service_client.client;

import com.example.user_service_client.dto.UserLoginDto;
import com.example.user_service_client.dto.UserRegisterDto;
import com.example.user_service_client.dto.UserResponseDto;
import com.example.user_service_client.dto.UserRoleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "user-service-app",
        contextId = "userClient",
        path = "/api/users",
        url = "http://api-gateway:8080"
)
public interface UserServiceClient {

    @GetMapping("/id/{id}")
    UserResponseDto getUserById(@PathVariable("id") Long id);

    @GetMapping("/username/{username}")
    UserResponseDto getUserByUsername(@PathVariable("username") String username);

    @GetMapping("/role/{role}")
    List<UserRoleResponse> getUsersByRole(@PathVariable("role") String role);

    @PostMapping("/register")
    String register(@RequestBody UserRegisterDto dto);

    @PostMapping("/login")
    String login(@RequestBody UserLoginDto dto);
}
