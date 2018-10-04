package com.thirdwayv.westpharma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WestPharmaApplication {

	public static void main(String[] args) {
		SpringApplication.run(WestPharmaApplication.class, args);
	}
}
