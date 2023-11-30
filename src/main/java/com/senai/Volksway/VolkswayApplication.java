package com.senai.Volksway;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "API Volksway", version = "1.0.0",description = "Swagger da API do Volksway"))
public class VolkswayApplication {

	public static void main(String[] args) {
		SpringApplication.run(VolkswayApplication.class, args);
	}

}