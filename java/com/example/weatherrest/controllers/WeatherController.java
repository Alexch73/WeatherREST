package com.example.weatherrest.controllers;

import com.example.weatherrest.dao.WeatherDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.IOException;
@Controller
public class WeatherController {

    private final WeatherDAO weatherDAO;
    @Autowired
    public WeatherController(WeatherDAO weatherDAO) {
        this.weatherDAO = weatherDAO;
    }

    @GetMapping("/weather")
    public String show(Model model) throws IOException {
        model.addAttribute("weather", weatherDAO.show());
        return "show";
    }
    }

