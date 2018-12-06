package com.lab.growbox.growbox.utils;

import arduino.Arduino;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Scanner;

public class ArduinoConnecter extends Thread {

    public void run() {
        Scanner scanner = new Scanner(System.in);
        Arduino arduino = new Arduino("COM7", 9600);
        boolean connected = arduino.openConnection();
        System.out.println("Соединение установлено: " + connected);
        while (true) {

            System.out.println("data:");

//            if (scanner.hasNext()) {

                String s = scanner.nextLine();

                System.out.println(s);
//            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
