package com.example.controller;

import com.example.entity.AcademicPaper;
import com.example.entity.User;
import com.example.service.AcademicPaperService;
import com.example.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/paper")
public class AcademicPaperController {
    private final AcademicPaperService paperService;
    private final FileService fileService;

    public AcademicPaperController(AcademicPaperService paperService, FileService fileService) {
        this.paperService = paperService;
        this.fileService = fileService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("list", paperService.findAll());
        return "paper/list";
    }

    @GetMapping("/add")
    public String addPage() {
        return "paper/add";
    }

    @PostMapping("/add")
    public String add(AcademicPaper paper,
            @RequestParam(value = "files", required = false) MultipartFile[] files,
            HttpSession session,
            RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        paper.setSubmitter(user.getRealName());
        int id = paperService.add(paper);
        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        fileService.upload(file, "paper", "paper", id);
                    } catch (IOException e) {
                        ra.addFlashAttribute("msg", "文件上传失败: " + e.getMessage());
                        return "redirect:/paper";
                    }
                }
            }
        }
        ra.addFlashAttribute("msg", "提交成功");
        return "redirect:/paper";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Integer id, Model model) {
        model.addAttribute("p", paperService.findById(id));
        return "paper/edit";
    }

    @PostMapping("/edit")
    public String edit(AcademicPaper paper, RedirectAttributes ra) {
        paperService.update(paper);
        ra.addFlashAttribute("msg", "修改成功");
        return "redirect:/paper";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        paperService.delete(id);
        ra.addFlashAttribute("msg", "删除成功");
        return "redirect:/paper";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("p", paperService.findById(id));
        model.addAttribute("files", paperService.getFiles(id));
        model.addAttribute("reviews", paperService.getReviewRecords(id));
        return "paper/detail";
    }

    @PostMapping("/upload/{id}")
    public String uploadFile(@PathVariable Integer id,
            @RequestParam("files") MultipartFile[] files,
            RedirectAttributes ra) {
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    fileService.upload(file, "paper", "paper", id);
                } catch (IOException e) {
                    ra.addFlashAttribute("msg", "文件上传失败: " + e.getMessage());
                    return "redirect:/paper/detail/" + id;
                }
            }
        }
        ra.addFlashAttribute("msg", "上传成功");
        return "redirect:/paper/detail/" + id;
    }

    @PostMapping("/review/{id}")
    public String review(@PathVariable Integer id,
            @RequestParam String reviewer,
            @RequestParam String reviewLevel,
            @RequestParam String status,
            @RequestParam String opinion,
            RedirectAttributes ra) {
        paperService.review(id, reviewer, reviewLevel, status, opinion);
        ra.addFlashAttribute("msg", "审核完成");
        return "redirect:/paper/detail/" + id;
    }
}
