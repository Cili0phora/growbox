package com.lab.growbox.growbox.controller;

import com.lab.growbox.growbox.entity.Data;
import com.lab.growbox.growbox.entity.Settings;
import com.lab.growbox.growbox.repository.DataRepository;
import com.lab.growbox.growbox.utils.ArduinoConnecter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            labels[i] = new Time(d.getTime()).toString();
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

    @PostMapping("updateWatering")
    public String updateWatering(HttpServletRequest request, HttpServletResponse response, Model model) {
        Settings settings = new Settings();
        settings.setWtTime(parseTime(request.getParameter("time")));
        settings.setPeriod(Integer.parseInt(request.getParameter("period")));
        settings.setcTime(parseTime(request.getParameter("cTime")));
        settings.setcDate(parseDate(request.getParameter("cDate")));
        ArduinoConnecter.send(settings);
        return "redirect:settings";
    }

    private Time parseTime(String str) {
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        Time time = null;
        try {
            time = new Time(formatter.parse(str).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    private Date parseDate(String str) {
        DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        Date date = null;
        try {
            date = new Date(formatter.parse(str).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
