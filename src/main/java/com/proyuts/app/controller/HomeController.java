package com.proyuts.app.controller;

import com.proyuts.app.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final DashboardService dashboardService;

    public HomeController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("resumen", dashboardService.obtenerResumen());
        return "index";
    }
}