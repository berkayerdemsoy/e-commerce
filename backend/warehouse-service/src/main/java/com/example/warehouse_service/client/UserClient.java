//package com.example.warehouse_service.client;
//
//import com.example.warehouse_service.config.FeignConfig;
//import com.example.warehouse_service.dto.shared.UserDto;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//@FeignClient(name = "user-service",url ="http://api-gateway:8080",configuration = FeignConfig.class)
//public interface UserClient {
//
//    @GetMapping("/api/users/id/{id}")
//    UserDto getUserById(@PathVariable Long id);
//    @GetMapping("api/users/role/{role}")
//    UserDto getUsersByRole(@PathVariable String role);
//
//
//}
