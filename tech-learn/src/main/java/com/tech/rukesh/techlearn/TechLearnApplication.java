package com.tech.rukesh.techlearn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
@EnableAsync
@SpringBootApplication
public class TechLearnApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechLearnApplication.class, args);
	}

}
