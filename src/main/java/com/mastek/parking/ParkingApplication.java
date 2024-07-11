package com.mastek.parking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class ParkingApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(ParkingApplication.class, args);
		log.info("Running profile: {}", context.getEnvironment().getProperty("spring.profiles.active"));
	}

}
