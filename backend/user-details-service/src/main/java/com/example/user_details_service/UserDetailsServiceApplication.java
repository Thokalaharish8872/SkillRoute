package com.example.user_details_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserDetailsServiceApplication {

	public static void main(String[] args) {
		loadEnvFile();
		SpringApplication.run(UserDetailsServiceApplication.class, args);
	}


	private static void loadEnvFile() {

		Dotenv dotenv = Dotenv.load();

		System.setProperty("JWT_SUPER_SECRET_KEY", dotenv.get("JWT_SUPER_SECRET_KEY"));
	}

}

