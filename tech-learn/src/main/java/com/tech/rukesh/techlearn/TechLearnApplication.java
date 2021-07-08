package com.tech.rukesh.techlearn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
@EnableScheduling
@EnableJpaRepositories
@EnableTransactionManagement
@EnableSwagger2
@EnableAsync
@SpringBootApplication
public class TechLearnApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechLearnApplication.class, args);
	}

}
