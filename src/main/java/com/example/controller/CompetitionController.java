package com.example.controller;

import com.example.entity.Competition;
import com.example.entity.User;
import com.example.service.CompetitionService;
import com.example.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/competition")
public class CompetitionController {
    private final CompetitionService competitionService;
    private final FileService fileService;

    public CompetitionController(CompetitionService competitionService, FileService fileService) {
        this.competitionService = competitionService;
        this.fileService = fileService;
    }

    @GetMapping
    public String list(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Competition> all = competitionService.findAll();
        if ("student".equals(user.getRole())) {
            String name = user.getRealName();
            all = all.stream()
                    .filter(c -> name.equals(c.getSubmitter()) || isInList(name, c.getParticipants()))
                    .collect(Collectors.toList());
        }
        model.addAttribute("list", all);
        return "competition/list";
    }

    @GetMapping("/add")
    public String addPage() {
        return "competition/add";
    }

    @PostMapping("/add")
    public String add(Competition competition,
            @RequestParam(value = "files", required = false) MultipartFile[] files,
            HttpSession session,
            RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        competition.setSubmitter(user.getRealName());
        int id = competitionService.add(competition);
        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        fileService.upload(file, "competition", "competition", id);
                    } catch (IOException e) {
                        ra.addFlashAttribute("msg", "文件上传失败: " + e.getMessage());
                        return "redirect:/competition";
                    }
                }
            }
        }
        ra.addFlashAttribute("msg", "提交成功");
        return "redirect:/competition";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Integer id, Model model, HttpSession session, RedirectAttributes ra) {
        Competition c = competitionService.findById(id);
        User user = (User) session.getAttribute("user");
        if ("student".equals(user.getRole())) {
            if (!user.getRealName().equals(c.getSubmitter())) {
                ra.addFlashAttribute("msg", "您不是提交人，无法修改");
                return "redirect:/competition";
            }
            if (!"pending".equals(c.getStatus())) {
                ra.addFlashAttribute("msg", "该成果已审核，无法修改");
                return "redirect:/competition";
            }
        }
        model.addAttribute("c", c);
        return "competition/edit";
    }

    @PostMapping("/edit")
    public String edit(Competition competition, HttpSession session, RedirectAttributes ra) {
        Competition existing = competitionService.findById(competition.getId());
        User user = (User) session.getAttribute("user");
        if ("student".equals(user.getRole())) {
            if (!user.getRealName().equals(existing.getSubmitter())) {
                ra.addFlashAttribute("msg", "您不是提交人，无法修改");
                return "redirect:/competition";
            }
            if (!"pending".equals(existing.getStatus())) {
                ra.addFlashAttribute("msg", "该成果已审核，无法修改");
                return "redirect:/competition";
            }
        }
        competitionService.update(competition);
        ra.addFlashAttribute("msg", "修改成功");
        return "redirect:/competition";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session, RedirectAttributes ra) {
        Competition c = competitionService.findById(id);
        User user = (User) session.getAttribute("user");
        if ("student".equals(user.getRole())) {
            if (!user.getRealName().equals(c.getSubmitter())) {
                ra.addFlashAttribute("msg", "您不是提交人，无法删除");
                return "redirect:/competition";
            }
            if (!"pending".equals(c.getStatus())) {
                ra.addFlashAttribute("msg", "该成果已审核，无法删除");
                return "redirect:/competition";
            }
        }
        competitionService.delete(id);
        ra.addFlashAttribute("msg", "删除成功");
        return "redirect:/competition";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("c", competitionService.findById(id));
        model.addAttribute("files", competitionService.getFiles(id));
        model.addAttribute("reviews", competitionService.getReviewRecords(id));
        return "competition/detail";
    }

    @PostMapping("/upload/{id}")
    public String uploadFile(@PathVariable Integer id,
            @RequestParam("files") MultipartFile[] files,
            RedirectAttributes ra) {
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    fileService.upload(file, "competition", "competition", id);
                } catch (IOException e) {
                    ra.addFlashAttribute("msg", "文件上传失败: " + e.getMessage());
                    return "redirect:/competition/detail/" + id;
                }
            }
        }
        ra.addFlashAttribute("msg", "上传成功");
        return "redirect:/competition/detail/" + id;
    }

    @PostMapping("/review/{id}")
    public String review(@PathVariable Integer id,
            @RequestParam String reviewer,
            @RequestParam String reviewLevel,
            @RequestParam String status,
            @RequestParam String opinion,
            RedirectAttributes ra) {
        competitionService.review(id, reviewer, reviewLevel, status, opinion);
        ra.addFlashAttribute("msg", "审核完成");
        return "redirect:/competition/detail/" + id;
    }

    private boolean isInList(String name, String list) {
        if (list == null || list.isEmpty()) return false;
        for (String s : list.split(",")) {
            if (s.trim().equals(name)) return true;
        }
        return false;
    }
}
