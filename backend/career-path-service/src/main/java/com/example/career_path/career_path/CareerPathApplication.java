package com.example.career_path.career_path;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
public class CareerPathApplication {

	public static void main(String[] args) {
		loadEnvFile();
		SpringApplication.run(CareerPathApplication.class, args);
	}

	private static void loadEnvFile() {
		Dotenv dotenv = Dotenv.load();

		System.setProperty("APPLICATION_NAME", dotenv.get("APPLICATION_NAME"));
		System.setProperty("SERVER_PORT", dotenv.get("SERVER_PORT"));
		System.setProperty("DB_URL", dotenv.get("DB_URL"));
		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		System.setProperty("DB_DRIVER", dotenv.get("DB_DRIVER"));
		System.setProperty("DDL_AUTO", dotenv.get("DDL_AUTO"));
		System.setProperty("SHOW_SQL", dotenv.get("SHOW_SQL"));
		System.setProperty("DB_PLATFORM", dotenv.get("DB_PLATFORM"));
		System.setProperty("REGISTER_WITH_EUREKA", dotenv.get("REGISTER_WITH_EUREKA"));
		System.setProperty("FETCH_REGISTRY", dotenv.get("FETCH_REGISTRY"));
		System.setProperty("EUREKA_SERVER_URL", dotenv.get("EUREKA_SERVER_URL"));
		System.setProperty("REDIS_HOST", dotenv.get("REDIS_HOST"));
		System.setProperty("REDIS_PORT", dotenv.get("REDIS_PORT"));
		System.setProperty("JWT_SUPER_SECRET_KEY", dotenv.get("JWT_SUPER_SECRET_KEY"));
	}
}