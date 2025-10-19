package com.example.user_service_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientProperties;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.user_service_client",
        defaultConfiguration = FeignClientProperties.FeignClientConfiguration.class)
public class UserServiceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceAppApplication.class, args);
	}

}
