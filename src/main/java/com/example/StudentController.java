package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StudentController {
    private final StudentDao dao;

    public StudentController(StudentDao dao) {
        this.dao = dao;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("students", dao.findAll());
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