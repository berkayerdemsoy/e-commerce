package com.example.user_service_app.controller;

import com.example.user_service_client.dto.UserRegisterDto;
import com.example.user_service_client.dto.UserResponseDto;
import com.example.user_service_client.dto.UserRoleResponse;
import com.example.user_service_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") Long id) {
         UserResponseDto user =userService.getUserById(id);
         return ResponseEntity.ok(user);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable("username") String username) {
        UserResponseDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") Long id){
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(Pageable pageable){
        Page<UserResponseDto> users =  userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUserById(@PathVariable("id") Long id , @Valid @RequestBody UserRegisterDto dto){
        UserResponseDto updated =  userService.updateUserById(id, dto);
        return ResponseEntity.ok(updated);
    }
    @GetMapping("/api/users/role/{role}")
    public ResponseEntity<List<UserRoleResponse>> getUsersByRole(@PathVariable("role") String role) {
        List<UserRoleResponse> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }


}
