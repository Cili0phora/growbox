package com.lab.growbox.growbox.utils;

import arduino.Arduino;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lab.growbox.growbox.entity.Data;

import java.sql.Time;
public class ArduinoConnecter  extends Thread  {

    private JDBCConnection jdbcConnection = new JDBCConnection();
    private Gson gson = new GsonBuilder().setDateFormat("dd.mm.yyyy HH:mm:ss").create();
    private Arduino arduino = new Arduino("COM7", 9600);

    public void run() {

//        String s = "{\"date\":\"04.00.2018 11:20:00\",\"temperature\":20.0,\"groundHum\":3,\"airHum\":11.0,\"waterLevel\":60,\"bright\":55.0}";
//        Data data = gson.fromJson(s, Data.class);
//        data.setTime(new Time(data.getDate().getTime()));
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
            s += readed.substring(0, readed.length()-1);
            if (!s.isEmpty() && s.substring(s.length() - 1).contains("}")){
                System.out.println("----" + s);
                Data data = gson.fromJson(s, Data.class);
                jdbcConnection.save(data);
                s = "";
            }
        }
    }
}
