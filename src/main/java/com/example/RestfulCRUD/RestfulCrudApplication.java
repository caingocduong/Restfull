package com.example.RestfulCRUD;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication(scanBasePackages = {"com.example"})
@EntityScan("com.example.entities")
@EnableJpaRepositories(basePackages="com.example.repositories")
@EnableGlobalMethodSecurity(securedEnabled = true)
public class RestfulCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestfulCrudApplication.class, args);
	}
	
}
