package com.lab.growbox.growbox;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lab.growbox.growbox.entity.Data;
import com.lab.growbox.growbox.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 * Подгрузка тестовых данных из базы
 */
@Component
public class DataLoader  implements ApplicationRunner {
    @Autowired
    private DataRepository dataRepository;

    public void run(ApplicationArguments args) {
        List<Data> dataList = dataRepository.findAll();
        if( dataList.isEmpty()) {
            Data data = new Data();
            data.setAirHum(11);
            data.setDate(new Date(2018, 4, 4));
            data.setTime(new Time(11, 20, 0));
            data.setTemperature(20);
            data.setGroundHum(3);
            data.setWaterLevel(60);
            data.setBright(55);
            dataRepository.save(data);

            Gson gson = new GsonBuilder().setDateFormat("dd.mm.yyyy HH:mm:ss").create();
            System.out.println(gson.toJson(data));

            data = new Data();
            data.setAirHum(12);
            data.setDate(new Date(2018, 4, 4));
            data.setTime(new Time(11, 20, 5));
            data.setTemperature(21);
            data.setGroundHum(4);
            data.setWaterLevel(50);
            data.setBright(55);
            dataRepository.save(data);

            data = new Data();
            data.setAirHum(13);
            data.setDate(new Date(2018, 4, 4));
            data.setTime(new Time(11, 20, 10));
            data.setTemperature(25);
            data.setGroundHum(4);
            data.setWaterLevel(40);
            data.setBright(55);
            dataRepository.save(data);

            data = new Data();
            data.setAirHum(16);
            data.setDate(new Date(2018, 4, 4));
            data.setTime(new Time(11, 20, 15));
            data.setTemperature(26);
            data.setGroundHum(5);
            data.setWaterLevel(40);
            data.setBright(55);
            dataRepository.save(data);

            data = new Data();
            data.setAirHum(16);
            data.setDate(new Date(2018, 4, 4));
            data.setTime(new Time(11, 20, 20));
            data.setTemperature(26);
            data.setGroundHum(5);
            data.setWaterLevel(50);
            data.setBright(55);
            dataRepository.save(data);


            data = new Data();
            data.setAirHum(11);
            data.setDate(new Date(2018, 4, 4));
            data.setTime(new Time(11, 20, 0));
            data.setTemperature(20);
            data.setGroundHum(3);
            data.setWaterLevel(60);
            data.setBright(55);
            dataRepository.save(data);

            data = new Data();
            data.setAirHum(12);
            data.setDate(new Date(2018, 4, 4));
            data.setTime(new Time(11, 20, 5));
            data.setTemperature(21);
            data.setGroundHum(4);
            data.setWaterLevel(50);
            data.setBright(55);
            dataRepository.save(data);

            data = new Data();
            data.setAirHum(13);
            data.setDate(new Date(2018, 4, 4));
            data.setTime(new Time(11, 20, 10));
            data.setTemperature(25);
            data.setGroundHum(4);
            data.setWaterLevel(40);
            data.setBright(55);
            dataRepository.save(data);

            data = new Data();
            data.setAirHum(16);
            data.setDate(new Date(2018, 4, 4));
            data.setTime(new Time(11, 20, 15));
            data.setTemperature(26);
            data.setGroundHum(5);
            data.setWaterLevel(40);
            data.setBright(55);
            dataRepository.save(data);

            data = new Data();
            data.setAirHum(16);
            data.setDate(new Date(2018, 4, 4));
            data.setTime(new Time(11, 20, 20));
            data.setTemperature(26);
            data.setGroundHum(2);
            data.setWaterLevel(20);
            data.setBright(55);
            dataRepository.save(data);
        }
    }
}
