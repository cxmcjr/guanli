package com.example.controller;

import com.example.entity.User;
import com.example.service.StatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {
    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public String page(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if ("student".equals(user.getRole())) {
            String name = user.getRealName();
            model.addAttribute("overview", statisticsService.getOverview(name));
            model.addAttribute("competitionStats", statisticsService.getCompetitionStats(name));
            model.addAttribute("innovationStats", statisticsService.getInnovationStats(name));
            model.addAttribute("paperStats", statisticsService.getPaperStats(name));
        } else {
            model.addAttribute("overview", statisticsService.getOverview());
            model.addAttribute("competitionStats", statisticsService.getCompetitionStats());
            model.addAttribute("innovationStats", statisticsService.getInnovationStats());
            model.addAttribute("paperStats", statisticsService.getPaperStats());
        }
        return "statistics/index";
    }

    @GetMapping("/api/overview")
    @ResponseBody
    public Map<String, Object> apiOverview(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if ("student".equals(user.getRole())) {
            return statisticsService.getOverview(user.getRealName());
        }
        return statisticsService.getOverview();
    }

    @GetMapping("/api/competition")
    @ResponseBody
    public Map<String, Object> apiCompetition(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if ("student".equals(user.getRole())) {
            return statisticsService.getCompetitionStats(user.getRealName());
        }
        return statisticsService.getCompetitionStats();
    }

    @GetMapping("/api/innovation")
    @ResponseBody
    public Map<String, Object> apiInnovation(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if ("student".equals(user.getRole())) {
            return statisticsService.getInnovationStats(user.getRealName());
        }
        return statisticsService.getInnovationStats();
    }

    @GetMapping("/api/paper")
    @ResponseBody
    public Map<String, Object> apiPaper(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if ("student".equals(user.getRole())) {
            return statisticsService.getPaperStats(user.getRealName());
        }
        return statisticsService.getPaperStats();
    }
}
