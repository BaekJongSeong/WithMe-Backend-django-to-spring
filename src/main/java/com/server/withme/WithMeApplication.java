package com.server.withme;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
//@EnableBatchProcessing
public class WithMeApplication {

	public static void main(String[] args) {
		SpringApplication.run(WithMeApplication.class, args);
	}

}
