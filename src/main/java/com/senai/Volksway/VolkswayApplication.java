package com.senai.Volksway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.senai.Volksway.repositories")
@ComponentScan(basePackages = "com.senai.Volksway")
@EnableTransactionManagement
public class VolkswayApplication {

	public static void main(String[] args) {
		SpringApplication.run(VolkswayApplication.class, args);
	}

}