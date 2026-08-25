package com.example.career_path.career_path;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
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
		System.setProperty("DDL_AUTO", dotenv.get("DDL_AUTO"));
		System.setProperty("SHOW_SQL", dotenv.get("SHOW_SQL"));
		System.setProperty("DB_PLATFORM", dotenv.get("DB_PLATFORM"));
		System.setProperty("JWT_SUPER_SECRET_KEY", dotenv.get("JWT_SUPER_SECRET_KEY"));
	}
}

