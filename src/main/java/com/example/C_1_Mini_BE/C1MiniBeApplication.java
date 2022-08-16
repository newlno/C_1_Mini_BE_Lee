package com.example.C_1_Mini_BE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class C1MiniBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(C1MiniBeApplication.class, args);
	}

}
