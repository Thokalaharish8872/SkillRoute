package com.example.ai_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiServiceApplication {

	public static void main(String[] args) {
		loadEnvFile();
		SpringApplication.run(AiServiceApplication.class, args);
	}
	private static void loadEnvFile() {

		Dotenv dotenv = Dotenv.load();

		System.setProperty("GEMINI_API_KEY", dotenv.get("GEMINI_API_KEY"));
	}
}
