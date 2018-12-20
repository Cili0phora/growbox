package com.lab.growbox.growbox.controller;

import com.lab.growbox.growbox.entity.Data;
import com.lab.growbox.growbox.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private DataRepository dataRepository;

    private Gson gson = new Gson();

    @GetMapping("/")
    public String index(Model model) {
        List<Data> dataList = dataRepository.findTop5();
        dataList.forEach(System.out::println);
        Data data = dataList.get(dataList.size() - 1);

        int[] groundHum = new int[dataList.size()];
        float[] temperature = new float[dataList.size()];
        float[] airHum = new float[dataList.size()];
        String[] labels = new String[dataList.size()];

        for (int i = 0; i<=dataList.size()-1; i++) {
            Data d = dataList.get(i);
            groundHum[i] = d.getGroundHum();
            temperature[i] = d.getTemperature();
            airHum[i] = d.getAirHum();
            labels[i] = d.getTime().toString();
        }

        model.addAttribute("data", data);
        model.addAttribute("groundHum", Arrays.toString(groundHum));
        model.addAttribute("temperature", Arrays.toString(temperature));
        model.addAttribute("airHum", Arrays.toString(airHum));
        model.addAttribute("labels", gson.toJson(labels));
        return "index";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        return "settings";
    }

}
