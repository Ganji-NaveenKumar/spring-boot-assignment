package com.Naveen.security_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.Objects;


@SpringBootApplication
@EnableDiscoveryClient
public class SecurityServiceApplication {


	public static void main(String[] args){

		Dotenv dotenv=Dotenv.load();
		String username = Objects.requireNonNull(dotenv.get("MYSQL_USERNAME"), "MYSQL_USERNAME is missing");
		String password = Objects.requireNonNull(dotenv.get("MYSQL_PASSWORD"), "MYSQL_PASSWORD is missing");
		System.setProperty("MYSQL_USERNAME", username);
		System.setProperty("MYSQL_PASSWORD", password);
		System.setProperty("JWT_SECRET_KEY", (Objects.requireNonNull(dotenv.get("JWT_SECRET_KEY"))));
		SpringApplication.run(SecurityServiceApplication.class, args);
	}

}
