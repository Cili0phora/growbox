package com.lab.growbox.growbox.controller;

import com.lab.growbox.growbox.entity.Data;
import com.lab.growbox.growbox.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        @GetMapping("/")
    public String greeting(Model model) {
        List<Data> dataList = dataRepository.findAll();
        Data data = dataList.get(0);
        model.addAttribute("waterLevel", data.getWaterLevel());
        return "index";
    }
}
