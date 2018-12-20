package com.lab.growbox.growbox.utils;

import arduino.Arduino;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lab.growbox.growbox.entity.Data;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ArduinoConnecter  extends Thread  {

    private JDBCConnection jdbcConnection = new JDBCConnection();
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .registerTypeAdapter(Time.class, new TimeDeserializer())
            .create();
    private Arduino arduino = new Arduino("COM7", 9600);

    public void run() {
//        String s = "{\"time\":\"04:35:28\",\"date\":\"09.01.2000\",\"temperature\":24.4,\"groundHum\":1015,\"airHum\":31.6,\"waterLevel\":57,\"bright\":2}";
//        Data data = gson.fromJson(s, Data.class);
//        jdbcConnection.save(data);

        boolean connected = arduino.openConnection();
        System.out.println("Соединение установлено: " + connected);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String s = "";
        while (true) {
            String readed = arduino.serialRead();
            if (!readed.isEmpty()) {
                s += readed.substring(0, readed.length() - 1);
                if (!s.isEmpty() && s.substring(s.length() - 1).contains("}")) {
                    System.out.println("----" + s);
                    try {
                        Data data = gson.fromJson(s, Data.class);
                        jdbcConnection.save(data);
                    } catch (Exception ex) {

                    } finally {
                        s = "";
                    }
                }
            }
        }
    }
}
