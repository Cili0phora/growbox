package com.lab.growbox.growbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GrowboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrowboxApplication.class, args);
	}
}
