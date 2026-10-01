package com.example.auth_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthServiceApplication {

	static {
		loadEnvFile();
	}

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	private static void loadEnvFile() {
		try {
			Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

			if (dotenv.get("APPLICATION_NAME") != null) System.setProperty("APPLICATION_NAME", dotenv.get("APPLICATION_NAME"));
			if (dotenv.get("SERVER_PORT") != null) System.setProperty("SERVER_PORT", dotenv.get("SERVER_PORT"));
			if (dotenv.get("DB_URL") != null) System.setProperty("DB_URL", dotenv.get("DB_URL"));
			if (dotenv.get("DB_USERNAME") != null) System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
			if (dotenv.get("DB_PASSWORD") != null) System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
			if (dotenv.get("DB_DRIVER") != null) System.setProperty("DB_DRIVER", dotenv.get("DB_DRIVER"));
			if (dotenv.get("DDL_AUTO") != null) System.setProperty("DDL_AUTO", dotenv.get("DDL_AUTO"));
			if (dotenv.get("SHOW_SQL") != null) System.setProperty("SHOW_SQL", dotenv.get("SHOW_SQL"));
			if (dotenv.get("DB_PLATFORM") != null) System.setProperty("DB_PLATFORM", dotenv.get("DB_PLATFORM"));
			if (dotenv.get("REGISTER_WITH_EUREKA") != null) System.setProperty("REGISTER_WITH_EUREKA", dotenv.get("REGISTER_WITH_EUREKA"));
			if (dotenv.get("FETCH_REGISTRY") != null) System.setProperty("FETCH_REGISTRY", dotenv.get("FETCH_REGISTRY"));
			if (dotenv.get("EUREKA_SERVER_URL") != null) System.setProperty("EUREKA_SERVER_URL", dotenv.get("EUREKA_SERVER_URL"));
			if (dotenv.get("JWT_SUPER_SECRET_KEY") != null) System.setProperty("JWT_SUPER_SECRET_KEY", dotenv.get("JWT_SUPER_SECRET_KEY"));
		} catch (Exception e) {
			// dotenv optional in test environments
		}
	}
}