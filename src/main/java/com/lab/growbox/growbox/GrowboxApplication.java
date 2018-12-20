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
//		Serial main = new Serial();
//		main.initialize();
//		Thread t=new Thread() {
//			public void run() {
//				//the following line will keep this app alive for 1000 seconds,
//				//waiting for events to occur and responding to them (printing incoming messages to console).
//				try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
//			}
//		};
//		t.start();
//		System.out.println("Started");
	}
}
