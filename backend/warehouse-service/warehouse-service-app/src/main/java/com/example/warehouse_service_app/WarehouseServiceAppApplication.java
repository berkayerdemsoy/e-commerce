package com.example.warehouse_service_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.example.warehouse_service_client.client",
"com.example.shop_service_client.client", "com.example.user_service_client.client"})
public class WarehouseServiceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(WarehouseServiceAppApplication.class, args);
	}

}
