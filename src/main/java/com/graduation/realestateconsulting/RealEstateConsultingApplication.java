package com.graduation.realestateconsulting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class
RealEstateConsultingApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealEstateConsultingApplication.class, args);
	}

}