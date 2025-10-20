package com.example.shop_service_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientProperties;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.shop_service_client.client",
        defaultConfiguration = com.example.common.config.FeignAutoConfiguration.class)
public class ShopServiceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopServiceAppApplication.class, args);
	}

}
