package com.example.payment_service_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients(basePackages = "com.example.payment_service_client.client",
defaultConfiguration = com.example.common.config.FeignAutoConfiguration.class)
public class PaymentServiceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentServiceAppApplication.class, args);
	}

}
