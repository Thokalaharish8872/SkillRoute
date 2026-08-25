package com.example.activity_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ActivityServiceApplication {

	public static void main(String[] args) {
		loadEnvFile();
		SpringApplication.run(ActivityServiceApplication.class, args);
	}
	private static void loadEnvFile() {

		Dotenv dotenv = Dotenv.load();

		System.setProperty("JWT_SUPER_SECRET_KEY", dotenv.get("JWT_SUPER_SECRET_KEY"));
	}
}
