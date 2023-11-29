package com.senai.Volksway;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.senai.Volksway.repositories")
@ComponentScan(basePackages = "com.senai.Volksway")
@EnableTransactionManagement
@OpenAPIDefinition(info = @Info(title = "API Volksway", version = "1.0.0",description = "Swagger da API do Volksway"))
public class VolkswayApplication {

	public static void main(String[] args) {
		SpringApplication.run(VolkswayApplication.class, args);
	}

}