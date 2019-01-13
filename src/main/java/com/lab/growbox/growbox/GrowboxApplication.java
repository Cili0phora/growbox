package com.lab.growbox.growbox;

import com.lab.growbox.growbox.utils.ArduinoConnecter;
import com.lab.growbox.growbox.utils.Serial;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GrowboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrowboxApplication.class, args);
		ArduinoConnecter arduinoConnecter = new ArduinoConnecter();
		arduinoConnecter.start();
	}
}
