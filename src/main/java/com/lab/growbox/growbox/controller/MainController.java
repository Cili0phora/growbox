package com.lab.growbox.growbox.controller;

import com.lab.growbox.growbox.entity.Data;
import com.lab.growbox.growbox.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private DataRepository dataRepository;

    @GetMapping("/halo")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        List<Data> dataList = dataRepository.findAll();
        Data data = dataList.get(0);
        model.addAttribute("name", data.getTemperature());
        return "halo";
    }

    private Gson gson = new Gson();

    @GetMapping("/")
    public String greeting(Model model) {
        List<Data> dataList = dataRepository.findTop5();
        dataList.forEach(System.out::println);
        Data data = dataList.get(dataList.size() - 1);
        int[] groundHum = new int[dataList.size()];
        String[] groundHumLabels = new String[dataList.size()];
        for (int i = 0; i<=dataList.size()-1; i++) {
            groundHum[i] = dataList.get(i).getGroundHum();
            groundHumLabels[i] = dataList.get(i).getTime().toString();
        }
        System.out.println(gson.toJson(groundHumLabels));
        System.out.println(Arrays.toString(groundHum));
        model.addAttribute("waterLevel", data.getWaterLevel());
        model.addAttribute("groundHum", Arrays.toString(groundHum));
        model.addAttribute("groundHumLabels", gson.toJson(groundHumLabels));
        return "index";
    }
}
