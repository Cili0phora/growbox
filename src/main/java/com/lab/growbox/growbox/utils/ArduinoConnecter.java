package com.lab.growbox.growbox.utils;

import arduino.Arduino;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lab.growbox.growbox.entity.Data;
import com.lab.growbox.growbox.entity.Settings;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ArduinoConnecter  extends Thread  {

    private JDBCConnection jdbcConnection = new JDBCConnection();
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .registerTypeAdapter(Time.class, new TimeDeserializer())
            .setDateFormat("dd.MM.yyyy")
            .create();
    private String port = "COM7";
//    private String port = "/dev/ttyACM0";
    private Arduino arduino = new Arduino(port, 9600);

    private static Settings request = null;

    public static void send(Settings settings) {
        request = settings;
    }

    public void run() {
//        String s = "{\"time\":\"30005000\",\"date\":\"09.01.2000\",\"temperature\":24.4,\"groundHum\":1015,\"airHum\":31.6,\"waterLevel\":57,\"bright\":2}";
//
//        Data data = gson.fromJson(s, Data.class);
//        System.out.println(data.toString());
//        jdbcConnection.save(data);
//
//        Settings settings = new Settings(5, new Time(11, 20, 5), new Time(12, 30, 00), new Date(2005, 12, 5));
//        System.out.println(gson.toJson(settings));

        boolean connected = arduino.openConnection();
        System.out.println("Соединение установлено: " + connected);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String s = "";
        while (connected) {
            if (request != null) {
                System.out.println("send data: " + request.toString());
                arduino.serialWrite(gson.toJson(request));
                request = null;
            }

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
