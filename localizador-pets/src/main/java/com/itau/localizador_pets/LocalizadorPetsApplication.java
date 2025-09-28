package com.itau.localizador_pets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.itau.localizador_pets.infrastructure.client")
public class LocalizadorPetsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocalizadorPetsApplication.class, args);
	}

}
