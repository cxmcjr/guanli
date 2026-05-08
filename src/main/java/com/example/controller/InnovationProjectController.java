package com.example.controller;

import com.example.entity.InnovationProject;
import com.example.entity.User;
import com.example.service.InnovationProjectService;
import com.example.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/innovation")
public class InnovationProjectController {
    private final InnovationProjectService projectService;
    private final FileService fileService;

    public InnovationProjectController(InnovationProjectService projectService, FileService fileService) {
        this.projectService = projectService;
        this.fileService = fileService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("list", projectService.findAll());
        return "innovation/list";
    }

    @GetMapping("/add")
    public String addPage() {
        return "innovation/add";
    }

    @PostMapping("/add")
    public String add(InnovationProject project,
            @RequestParam(value = "files", required = false) MultipartFile[] files,
            HttpSession session,
            RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        project.setSubmitter(user.getRealName());
        int id = projectService.add(project);
        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        fileService.upload(file, "innovation", "innovation", id);
                    } catch (IOException ignored) {
                    }
                }
            }
        }
        ra.addFlashAttribute("msg", "提交成功");
        return "redirect:/innovation";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Integer id, Model model) {
        model.addAttribute("p", projectService.findById(id));
        return "innovation/edit";
    }

    @PostMapping("/edit")
    public String edit(InnovationProject project, RedirectAttributes ra) {
        projectService.update(project);
        ra.addFlashAttribute("msg", "修改成功");
        return "redirect:/innovation";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        projectService.delete(id);
        ra.addFlashAttribute("msg", "删除成功");
        return "redirect:/innovation";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("p", projectService.findById(id));
        model.addAttribute("files", projectService.getFiles(id));
        model.addAttribute("reviews", projectService.getReviewRecords(id));
        return "innovation/detail";
    }

    @PostMapping("/upload/{id}")
    public String uploadFile(@PathVariable Integer id,
            @RequestParam("files") MultipartFile[] files,
            RedirectAttributes ra) {
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    fileService.upload(file, "innovation", "innovation", id);
                } catch (IOException ignored) {
                }
            }
        }
        ra.addFlashAttribute("msg", "上传成功");
        return "redirect:/innovation/detail/" + id;
    }

    @PostMapping("/review/{id}")
    public String review(@PathVariable Integer id,
            @RequestParam String reviewer,
            @RequestParam String reviewLevel,
            @RequestParam String status,
            @RequestParam String opinion,
            RedirectAttributes ra) {
        projectService.review(id, reviewer, reviewLevel, status, opinion);
        ra.addFlashAttribute("msg", "审核完成");
        return "redirect:/innovation/detail/" + id;
    }
}
