package com.example.shop_service_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.shop_service_client.client")
public class ShopServiceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopServiceAppApplication.class, args);
	}

}
