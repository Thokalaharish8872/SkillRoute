package com.example.ai_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AiServiceApplication {

	public static void main(String[] args) {
		loadEnvFile();
		SpringApplication.run(AiServiceApplication.class, args);
	}

	private static void loadEnvFile() {

		Dotenv dotenv = Dotenv.load();

		System.setProperty("APPLICATION_NAME", dotenv.get("APPLICATION_NAME"));
		System.setProperty("SERVER_PORT", dotenv.get("SERVER_PORT"));
		System.setProperty("REGISTER_WITH_EUREKA", dotenv.get("REGISTER_WITH_EUREKA"));
		System.setProperty("FETCH_REGISTRY", dotenv.get("FETCH_REGISTRY"));
		System.setProperty("EUREKA_SERVER_URL", dotenv.get("EUREKA_SERVER_URL"));
		System.setProperty("GEMINI_API_KEY", dotenv.get("GEMINI_API_KEY"));
		System.setProperty("REDIS_HOST", dotenv.get("REDIS_HOST"));
		System.setProperty("REDIS_PORT", dotenv.get("REDIS_PORT"));
		System.setProperty("JWT_SUPER_SECRET_KEY", dotenv.get("JWT_SUPER_SECRET_KEY"));
	}
}