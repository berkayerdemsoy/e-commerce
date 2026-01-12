package com.example.cart_service_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.example.cart_service_client.client",
        "com.example.shop_service_client.client"},
        defaultConfiguration = com.example.common.config.FeignAutoConfiguration.class)
@EnableRetry
public class CartServiceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartServiceAppApplication.class, args);
	}

}
