package com.example.skillroute_api_gateway;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SkillrouteApiGatewayApplication {

	public static void main(String[] args) {
		loadEnvFile();
		SpringApplication.run(SkillrouteApiGatewayApplication.class, args);
	}

	private static void loadEnvFile() {

		Dotenv dotenv = Dotenv.load();

		System.setProperty("APPLICATION_NAME", dotenv.get("APPLICATION_NAME"));
		System.setProperty("SERVER_PORT", dotenv.get("SERVER_PORT"));
		System.setProperty("REGISTER_WITH_EUREKA", dotenv.get("REGISTER_WITH_EUREKA"));
		System.setProperty("FETCH_REGISTRY", dotenv.get("FETCH_REGISTRY"));
		System.setProperty("EUREKA_SERVER_URL", dotenv.get("EUREKA_SERVER_URL"));
		System.setProperty("JWT_SUPER_SECRET_KEY", dotenv.get("JWT_SUPER_SECRET_KEY"));
		System.setProperty("PUBLIC_IP", dotenv.get("PUBLIC_IP"));
	}
}
