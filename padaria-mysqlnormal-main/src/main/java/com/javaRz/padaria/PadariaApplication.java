package com.javaRz.padaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication

@EnableMongoRepositories(basePackages = "com.javaRz.padaria.infrastructure.repository")
public class PadariaApplication {
	public static void main(String[] args) {
		SpringApplication.run(PadariaApplication.class, args);
	}
}