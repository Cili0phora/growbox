package com.lab.growbox.growbox;

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
            data.setTime(new Time(11, 20, 00));
            data.setTemperature(20);
            data.setGrountHum(3);
            Data saved = dataRepository.save(data);
            System.out.println("SAVE: " + saved.toString());
        }
    }
}
