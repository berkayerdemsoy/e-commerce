package com.example.cart_service_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.cart_service_client.client",
        defaultConfiguration = com.example.common.config.FeignAutoConfiguration.class)
public class CartServiceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartServiceAppApplication.class, args);
	}

}
