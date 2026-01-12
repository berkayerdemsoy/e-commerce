package com.example.user_service_client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleResponse {
    private Long id;
    private String username;
    private String email;
    private String role;


}
