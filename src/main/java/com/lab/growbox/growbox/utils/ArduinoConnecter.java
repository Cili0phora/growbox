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
            String s = arduino.serialRead();
            System.out.println(s);
        }
    }
}
