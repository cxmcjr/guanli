package com.example.controller;

import com.example.service.StatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {
    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public String page(Model model) {
        model.addAttribute("overview", statisticsService.getOverview());
        model.addAttribute("competitionStats", statisticsService.getCompetitionStats());
        model.addAttribute("innovationStats", statisticsService.getInnovationStats());
        model.addAttribute("paperStats", statisticsService.getPaperStats());
        return "statistics/index";
    }

    @GetMapping("/api/overview")
    @ResponseBody
    public Map<String, Object> apiOverview() {
        return statisticsService.getOverview();
    }

    @GetMapping("/api/competition")
    @ResponseBody
    public Map<String, Object> apiCompetition() {
        return statisticsService.getCompetitionStats();
    }

    @GetMapping("/api/innovation")
    @ResponseBody
    public Map<String, Object> apiInnovation() {
        return statisticsService.getInnovationStats();
    }

    @GetMapping("/api/paper")
    @ResponseBody
    public Map<String, Object> apiPaper() {
        return statisticsService.getPaperStats();
    }
}
