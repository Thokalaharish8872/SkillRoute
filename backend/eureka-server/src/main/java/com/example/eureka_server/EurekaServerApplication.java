package com.example.eureka_server;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

	public static void main(String[] args) {

		loadEnv();
		SpringApplication.run(EurekaServerApplication.class, args);
	}

	private static void loadEnv() {
		Dotenv dotenv = Dotenv.load();

		System.setProperty("APPLICATION_NAME", dotenv.get("APPLICATION_NAME"));
		System.setProperty("SERVER_PORT", dotenv.get("SERVER_PORT"));
		System.setProperty("REGISTER_WITH_EUREKA", dotenv.get("REGISTER_WITH_EUREKA"));
		System.setProperty("FETCH_REGISTRY", dotenv.get("FETCH_REGISTRY"));
	}

}
