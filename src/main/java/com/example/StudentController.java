package com.example;

import com.example.service.StatisticsService;
import com.example.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@Controller
public class StudentController {
    private final StudentDao dao;
    private final StatisticsService statisticsService;

    public StudentController(StudentDao dao, StatisticsService statisticsService) {
        this.dao = dao;
        this.statisticsService = statisticsService;
    }

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("students", dao.findAll());
        if ("student".equals(user.getRole())) {
            model.addAttribute("overview", statisticsService.getOverview(user.getRealName()));
        } else {
            model.addAttribute("overview", statisticsService.getOverview());
        }
        return "index";
    }

    @GetMapping("/add")
    public String addPage() {
        return "add";
    }

    @PostMapping("/add")
    public String add(Student student) {
        dao.add(student);
        return "redirect:/";
    }

    @GetMapping("/edit/{stuId}")
    public String editPage(@PathVariable String stuId, Model model) {
        model.addAttribute("s", dao.findByStuId(stuId));
        return "edit";
    }

    @PostMapping("/edit")
    public String edit(Student student) {
        dao.update(student);
        return "redirect:/";
    }

    @GetMapping("/delete/{stuId}")
    public String delete(@PathVariable String stuId) {
        dao.delete(stuId);
        return "redirect:/";
    }
}